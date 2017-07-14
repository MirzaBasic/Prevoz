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

   
    public class ChatController : Controller
    {
        private static int itemsPerPage = 15;
        MojContext mc = new MojContext();
        MessageController messageControler = new MessageController();
        // GET: Chat
        public JsonNetResult Pretraga(int Korisnik1Id, int Korisnik2Id, int page)
        {
           

            List<PorukeVM> svePoruke = mc.Poruke.Where(x => (x.KorisnikPoslaoId == Korisnik1Id && x.KorisnikPrimioId == Korisnik2Id) || (x.KorisnikPoslaoId == Korisnik2Id && x.KorisnikPrimioId == Korisnik1Id))
                    .Select(x => new PorukeVM {
                        Id=x.Id,
                        Data=x.Data,
                        DataType=x.DataType,
                        DatumKreiranja=x.DatumKreiranja,
                       KorisnikPoslaoId=x.KorisnikPoslaoId,
                       KorisnikPrimioId=x.KorisnikPrimioId,
                       Status=x.Status,
                       Text=x.Text

                    }).ToList().OrderByDescending(x => x.DatumKreiranja).ToList(); ;

            

            var model = svePoruke.Skip(page * itemsPerPage).Take(itemsPerPage).ToList();

          




            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = model };


        }

        public JsonNetResult Neprocitane(int Korisnik1Id, int Korisnik2Id)
        {
        

            List<PorukeVM> svePoruke = mc.Poruke.Where(x => ((x.KorisnikPoslaoId == Korisnik1Id && x.KorisnikPrimioId == Korisnik2Id) || (x.KorisnikPoslaoId == Korisnik2Id && x.KorisnikPrimioId == Korisnik1Id))&&(x.Status==0&&x.KorisnikPoslaoId==Korisnik2Id))
                    .Select(x => new PorukeVM
                    {
                        Id = x.Id,
                        Data = x.Data,
                        DataType = x.DataType,
                        DatumKreiranja = x.DatumKreiranja,
                        KorisnikPoslaoId = x.KorisnikPoslaoId,
                        KorisnikPrimioId = x.KorisnikPrimioId,
                        Status = x.Status,
                        Text = x.Text

                    }).ToList().OrderByDescending(x => x.DatumKreiranja).ToList(); ;



            foreach (PorukeVM item in svePoruke)
            {
                Poruka p = mc.Poruke.Find(item.Id);
                p.Status = 1;
            }

            mc.SaveChanges();
            var model = svePoruke;

           


            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = model };


        }



        public JsonNetResult Messenger(int KorisnikId,int page)
        {
           

            List<PorukeVM> svePoruke = mc.Poruke.Where(x => ((x.KorisnikPoslaoId == KorisnikId || x.KorisnikPrimioId == KorisnikId) && !x.IsDeleted))
                    .Select(x => new PorukeVM
                    {
                        Id = x.Id,
                        Data = x.Data,
                        DataType = x.DataType,
                        DatumKreiranja = x.DatumKreiranja,
                        KorisnikPoslaoId = x.KorisnikPoslaoId,
                        KorisnikPrimioId = x.KorisnikPrimioId,
                        Status = x.Status,
                        Text = x.Text


                    }).ToList();

           



            var model = svePoruke.Skip(page * itemsPerPage).Take(itemsPerPage).ToList();

            
            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = model };


        }

        public ActionResult Posalji(PorukeVM poruka) {

            Poruka p = new Poruka();
            p.Text = poruka.Text;
            p.Status = poruka.Status;
            p.KorisnikPoslaoId = poruka.KorisnikPoslaoId;
            p.KorisnikPrimioId = poruka.KorisnikPrimioId;
            p.Data = poruka.Data;
            p.DataType = poruka.DataType;
            p.DatumKreiranja = DateTime.Now;

            mc.Poruke.Add(p);

            try
            {
                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }

            if (p.KorisnikPoslaoId != p.KorisnikPrimioId) {
                Korisnik korisnik = mc.Korisnici.FirstOrDefault(x => x.Id == p.KorisnikPoslaoId);
            messageControler.Posalji(Notification.NOVA_PORUKA, korisnik.Id, korisnik.ImePrezime, korisnik.photoUrl, p.KorisnikPrimioId);
        }
            return new HttpStatusCodeResult(HttpStatusCode.OK);

        }


     

        
    }
}