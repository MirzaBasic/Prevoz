
using ePrevoz.DATA.Model;
using ePrevoz.WEB.Data;
using ePrevoz.WEB.Helper;
using ePrevoz.WEB.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;
using System.Web.Helpers;
using System.Web.Mvc;
using System.Web.Script.Serialization;

namespace ePrevoz.WEB.Controllers
{
    public class PrevoziController : Controller
    {
        MojContext mc = new MojContext();
        MessageController messageControler = new MessageController();
        // GET: Prevozi
        private static int itemsPerPage = 15;
   

        public JsonNetResult Pretraga(String startGrad, String krajGrad, int tipPrevoza,DateTime? datum, int page)
        {

          
            PrevozVM sviPrevozi = new PrevozVM();
            sviPrevozi.Prevozi = mc.Prevozi.Where(
                                    x => x.Aktivno 
                                    && x.DatumKretanja > DateTime.Now 
                                    && x.TipPrevoza == tipPrevoza
                                    &&((datum.HasValue&& DbFunctions.TruncateTime(x.DatumKretanja) == datum.Value)
                                    ||(!datum.HasValue)))
            .Select(x => new PrevozVM.Info
            {
                Id = x.Id,
                DatumKreiranja = x.DatumKreiranja,
                DatumKretanja = x.DatumKretanja,
                Cijena = x.Cijena,
                Opis = x.Opis,
                Aktivno = x.Aktivno,
                Telefon = x.Telefon,
                Korisnik = new KorisnikVM
                {

                    Id = x.Korisnik.Id,
                    Email = x.Korisnik.Email,
                    ImePrezime = x.Korisnik.ImePrezime,
                    photoUrl = x.Korisnik.photoUrl,
                    UserId = x.Korisnik.UserId,


                },

                BrojMjesta = x.BrojMjesta,
             
                PrevoznoSredstvo = x.PrevoznoSredstvo.Naziv,
                SlobodnoMjesto = x.SlobodnoMjesto,
                Stanice = mc.Stanice.Where(y => y.PrevozId == x.Id).Select(y => new StanicaVM
                {
                    Id = y.Id,
                    Grad = y.Grad,
                    RednaOdznaka = y.RednaOdznaka,
                    Lat = y.Lat,
                    Lng = y.Lng

                }).ToList()






            }).OrderByDescending(x => x.DatumKreiranja).ToList();

            
            
           List<PrevozVM.Info> prevoziPretraga = new List<PrevozVM.Info>();
            if (startGrad == "" && krajGrad == "")
            {

                prevoziPretraga = sviPrevozi.Prevozi;
            }
            else
            {
                foreach (PrevozVM.Info prevoz in sviPrevozi.Prevozi)
                {
                    StanicaVM stanicaStart = null;
                    StanicaVM stanicaKraj = null;
                    foreach (StanicaVM stanica in prevoz.Stanice)
                    {

                        if (stanica.Grad.Contains(startGrad) && startGrad != "")
                        {
                            stanicaStart = stanica;
                        }
                        if (stanica.Grad.Contains(krajGrad) && krajGrad != "")
                        {
                            stanicaKraj = stanica;
                        }

                    }
                    if (stanicaStart != null && stanicaKraj != null)
                    {
                        if (stanicaStart.RednaOdznaka < stanicaKraj.RednaOdznaka)
                            prevoziPretraga.Add(prevoz);
                    }

                    else if (stanicaStart != null && stanicaKraj == null && krajGrad == "")
                    {
                        if(prevoz.Stanice.LastOrDefault().Id!=stanicaStart.Id)
                        prevoziPretraga.Add(prevoz);
                    }
                    else if (stanicaKraj != null && stanicaStart == null && startGrad == "")
                    {
                        if (prevoz.Stanice.FirstOrDefault().Id != stanicaKraj.Id)
                            prevoziPretraga.Add(prevoz);
                    }


                }
            }

            var model = prevoziPretraga.Skip(page * itemsPerPage).Take(itemsPerPage).ToList();




            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = model};


        }

        public ActionResult IzbrisiPrevoz(int Id) {

            Prevoz p = mc.Prevozi.FirstOrDefault(x => x.Id == Id);
            List<Stanice> s = mc.Stanice.Where(x => x.PrevozId == p.Id).ToList();
            
            mc.Stanice.RemoveRange(s);
           
            mc.Prevozi.Remove(p);

            try
            {
                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            return new HttpStatusCodeResult(HttpStatusCode.OK);

        }

        public ActionResult Dodaj(PrevozVM.Info prevoz) {
            
            Prevoz prevozDB=mc.Prevozi.Where(x=>x.Id==prevoz.Id).FirstOrDefault();
            if (prevoz.Id == 0)
            {

                prevozDB= new Prevoz();
                mc.Prevozi.Add(prevozDB);


            }
            Korisnik korisnik=mc.Korisnici.Where(x => x.UserId == prevoz.Korisnik.UserId).FirstOrDefault();
            prevozDB.Cijena = prevoz.Cijena;
            prevozDB.BrojMjesta = prevoz.BrojMjesta;
            prevozDB.Opis = prevoz.Opis;
            prevozDB.KorisnikId = korisnik.Id;
            prevozDB.Aktivno = true;
            prevozDB.DatumKretanja = prevoz.DatumKretanja;
            prevozDB.Telefon = prevoz.Telefon;
            prevozDB.DatumKreiranja = DateTime.Now;
            prevozDB.SlobodnoMjesto = true;
            prevozDB.TipPrevoza = prevoz.TipPrevoza;
            prevozDB.PrevoznoSredstvoId = prevoz.PrevoznoSredstvoId;
          








            try
            {
                mc.SaveChanges();
            }
            catch {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }

            if (prevoz.Stanice != null)
            {
                foreach (StanicaVM item in prevoz.Stanice)
                {

                    mc.Stanice.Add(new Stanice {
                        Grad=item.Grad,
                        PrevozId=prevozDB.Id,
                        Lng=item.Lng,
                        Lat=item.Lat,
                        RednaOdznaka=item.RednaOdznaka

                    });

                }



                try
                {
                    mc.SaveChanges();
                }
                catch
                {
                    return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
                }
            }

            messageControler.PosaljiNaTopic(Notification.NOVI_PREVOZ,korisnik.ImePrezime, korisnik.photoUrl,Notification.PREVOZI_TOPIC);


            return new HttpStatusCodeResult(HttpStatusCode.OK);


        }
    }
}