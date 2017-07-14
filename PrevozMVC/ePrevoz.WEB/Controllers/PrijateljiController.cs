using ePrevoz.DATA.Model;
using ePrevoz.WEB.Data;
using ePrevoz.WEB.Helper;
using ePrevoz.WEB.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace ePrevoz.WEB.Controllers
{
    public class PrijateljiController : Controller
    {
        private static int PENDING = 0;
        private static int ACCEPTED = 1;
        private static int DECLINED = 2;
        private static int BLOCKED = 3;

        MojContext mc = new MojContext();


        MessageController messageControler = new MessageController();

        public JsonNetResult Pretraga(int id,String q,int Status)
        {
            List<Prijatelji> prijatelji = mc.Prijatelji.Where(x => (x.Korisnik1Id == id || x.Korisnik2Id == id)&&x.Status==Status).ToList();
            List<KorisnikVM> model = new List<KorisnikVM>();

            foreach (Prijatelji item in prijatelji)
            {
                if (item.Korisnik1Id== id)
                {
                    if (item.Korisnik2.ImePrezime.ToLower().Contains(q.ToLower()))
                    {

                        model.Add(new KorisnikVM
                        {
                          Id= item.Korisnik2.Id,
                          ImePrezime=item.Korisnik2.ImePrezime,
                          Email=item.Korisnik2.Email,
                          photoUrl=item.Korisnik2.photoUrl,
                           
                          
                        });
                    }
                }
                else {
                    if (item.Korisnik1.ImePrezime.ToLower().Contains(q.ToLower()))
                    {
                        model.Add(new KorisnikVM
                        {
                            Id = item.Korisnik1.Id,
                            ImePrezime = item.Korisnik1.ImePrezime,
                            Email = item.Korisnik1.Email,
                            photoUrl = item.Korisnik1.photoUrl,
                        

                        });
                    }
                }
            }


            var jsonModel = model;
            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = model };
        }
        public ActionResult Dodaj(PrijateljiVM prijatelj)
        {

            Prijatelji p = new Prijatelji();
            p.Korisnik1Id = prijatelj.Korisnik1Id;
            p.Korisnik2Id = prijatelj.Korisnik2Id;
            p.PoslaoKorisnikId = prijatelj.PoslaoKorisnikId;
            p.Status = PENDING;
           
            p.DatumKreiranja = DateTime.Now;
            mc.Prijatelji.Add(p);
            try
            {
                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }

            Korisnik k = mc.Korisnici.Find(p.PoslaoKorisnikId);

            messageControler.Posalji( Notification.ZAHTJEV_ZA_PRIJATELJSTVO, k.Id, k.ImePrezime,k.photoUrl, p.Korisnik2Id);

            return Json(p, JsonRequestBehavior.AllowGet);



        }

        public ActionResult Prihvati(int id)
        {

            Prijatelji p = mc.Prijatelji.Find(id);
            if (p != null)
            {
                p.Status = ACCEPTED;


                try
                {
                    mc.SaveChanges();
                }
                catch
                {
                    return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
                }


              
                messageControler.Posalji(Notification.PRIHVACEN_ZAHTJEV_ZA_PRIJATELJSTVO, p.Korisnik2Id, p.Korisnik2.ImePrezime,p.Korisnik2.photoUrl,p.Korisnik1Id);
                return new HttpStatusCodeResult(HttpStatusCode.OK);
            }
            else
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
        }
    

    public ActionResult Odbij(int id)
    {

        Prijatelji p = mc.Prijatelji.Find(id);
        if (p != null)
        {
            p.Status = DECLINED;


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
        else
        {
            return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
        }
    }

        public ActionResult Block(int id)
        {

            Prijatelji p = mc.Prijatelji.Find(id);
            if (p != null)
            {
                p.Status = BLOCKED;


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
            else
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
        }

        public ActionResult Delete() {

            List<Prijatelji> p = mc.Prijatelji.ToList();

            mc.Prijatelji.RemoveRange(p);
            mc.SaveChanges();

            return new HttpStatusCodeResult(HttpStatusCode.OK);

        }

        public ActionResult Izbrisi(int id)
        {

            Prijatelji p = mc.Prijatelji.Find(id);
            if (p != null)
            {
                mc.Prijatelji.Remove(p);


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
            else
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
        }
    }
}