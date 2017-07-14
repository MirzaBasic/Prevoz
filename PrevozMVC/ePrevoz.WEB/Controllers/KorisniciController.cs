using ePrevoz.DATA.Model;
using ePrevoz.WEB.Data;
using ePrevoz.WEB.Helper;
using ePrevoz.WEB.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace ePrevoz.WEB.Controllers
{
    

    public class KorisniciController : Controller
    {
       
        MojContext mc = new MojContext();
        // GET: Korisnici
        public JsonNetResult Dodaj(KorisnikVM korisnik)
        {   


            Korisnik k = mc.Korisnici.Where(x => x.UserId == korisnik.UserId).FirstOrDefault();
            FirebaseToken token = new FirebaseToken();
            if (k==null)
            {
                k = new Korisnik();
                mc.Korisnici.Add(k);
            
            }
          
            k.Email = korisnik.Email;
            k.photoUrl = korisnik.photoUrl;
            k.coverPhotoUrl = korisnik.coverPhotoUrl;
            k.UserId = korisnik.UserId;
            k.ImePrezime = korisnik.ImePrezime;



            try
            {
                mc.SaveChanges();


                List<FirebaseToken> ft = mc.FirebaseTokens.Where(x => x.KorisnikId == k.Id).ToList();
                bool tokenExist = false;
                foreach (FirebaseToken item in ft)
                {
                    if (item.Token == korisnik.FirebaseToken)
                    {

                        tokenExist = true;
                        token = item;
                        break;

                    }
                }
                if (!tokenExist)
                {
                  
                    token.Token = korisnik.FirebaseToken;
                    token.KorisnikId = k.Id;
                    mc.FirebaseTokens.Add(token);


                    mc.SaveChanges();
                }

            }
            catch
            {
                return null;
            }

            korisnik.Id = k.Id;
            korisnik.FirebaseTokenId = token.Id;
         
            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = korisnik };


        }


        public JsonNetResult GetAll() {

           var model= mc.Korisnici.ToList();
            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = model};



        }

        public JsonNetResult Detalji(int id, int korisnikId)
        {

            
         
            KorisnikVM korisnik = mc.Korisnici.Where(x => x.Id == id).Select(x => new KorisnikVM
            {
                Id = x.Id,
                ImePrezime = x.ImePrezime,
                Email = x.Email,
                photoUrl = x.photoUrl,
                coverPhotoUrl = x.coverPhotoUrl,
             
                UserId = x.UserId
               



            }).FirstOrDefault();



            Prijatelji p = mc.Prijatelji.Where(x => (x.Korisnik1Id == id && x.Korisnik2Id == korisnikId) || (x.Korisnik1Id == korisnikId && x.Korisnik2Id == id)).FirstOrDefault();


            if (p != null)
            {
                korisnik.Prijatelj = new PrijateljiVM
                {

                    Id = p.Id,
                    PoslaoKorisnikId = p.PoslaoKorisnikId,
                    Status = p.Status

                };
            }
            var setting = new Newtonsoft.Json.JsonSerializerSettings();
            setting.ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver();

            return new JsonNetResult() { Data = korisnik  };



        }



        public ActionResult Delete( int id)
        {
            Korisnik k = mc.Korisnici.FirstOrDefault(x => x.Id == id);
            if (k != null) {
                mc.Korisnici.Remove(k);
                mc.SaveChanges();
              
            }


            return new HttpStatusCodeResult(HttpStatusCode.OK);
        }


        public ActionResult DeleteToken(int id)
        {
            FirebaseToken ft = mc.FirebaseTokens.FirstOrDefault(x => x.Id == id);
            if (ft != null)
            {
                mc.FirebaseTokens.Remove(ft);
                try
                {
                    mc.SaveChanges();
                }
                catch
                {
                    return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
                }

            }


            return new HttpStatusCodeResult(HttpStatusCode.OK);
        }


    }

}