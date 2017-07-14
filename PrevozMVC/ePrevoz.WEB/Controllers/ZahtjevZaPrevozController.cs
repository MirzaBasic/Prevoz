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
    public class ZahtjevZaPrevozController : Controller
    {
        MojContext mc = new MojContext();
        MessageController messageControler = new MessageController();
        // GET: Prevozi
        private static int itemsPerPage = 15;

        private static int PENDING = 0;
        private static int ACCEPTED = 1;
        private static int DECLINED = 2;
 
        // GET: ZahtjevZaPrevoz
        public ActionResult Posalji(ZahtjevZaPrevozVM zahtjev)
        {
            ZahtjevZaPrevoz zzp = new ZahtjevZaPrevoz();
            mc.ZahtjeviZaPrevoze.Add(zzp);
            zzp.DatumKreiranja = DateTime.Now;
            zzp.KorisnikId = zahtjev.KorisnikId;
            zzp.Kolicina = zahtjev.Kolicina;
            zzp.PrevozId = zahtjev.PrevozId;
            zzp.Status = PENDING;
           

            try {

                mc.SaveChanges();
            } catch {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);


            }


            Korisnik k = mc.Korisnici.FirstOrDefault(x => x.Id == zahtjev.KorisnikId);
            Prevoz p = mc.Prevozi.FirstOrDefault(x => x.Id == zahtjev.PrevozId);
            messageControler.Posalji(Notification.ZAHTJEV_ZA_PREVOZ, k.Id, k.ImePrezime, k.photoUrl, p.KorisnikId);
            return new HttpStatusCodeResult(HttpStatusCode.OK);

            
        }

        public ActionResult Prihvati(int ZahtjevZaPrevozId)
        {
            ZahtjevZaPrevoz zzp = mc.ZahtjeviZaPrevoze.FirstOrDefault(x => x.Id == ZahtjevZaPrevozId);
            zzp.Status = ACCEPTED;
            Prevoz p = mc.Prevozi.FirstOrDefault(x => x.Id == zzp.PrevozId);
            p.BrojZauzetihMjesta++;
            if (p.BrojMjesta == p.BrojZauzetihMjesta) {
                p.SlobodnoMjesto = false;
            }
            try
            {

                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);


            }


            messageControler.Posalji(Notification.ODOBREN_ZAHTJEV_ZA_PREVOZ, zzp.Prevoz.KorisnikId, zzp.Prevoz.Korisnik.ImePrezime, zzp.Prevoz.Korisnik.photoUrl, zzp.KorisnikId);
            return new HttpStatusCodeResult(HttpStatusCode.OK);

        }
        public ActionResult Odbij(int ZahtjevZaPrevozId)
        {
            ZahtjevZaPrevoz zzp = mc.ZahtjeviZaPrevoze.FirstOrDefault(x => x.Id == ZahtjevZaPrevozId);
            zzp.Status =DECLINED ;
           

            try
            {

                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);


            }


            messageControler.Posalji(Notification.ODBIJEN_ZAHTJEV_ZA_PREVOZ, zzp.Prevoz.KorisnikId, zzp.Prevoz.Korisnik.ImePrezime, zzp.Prevoz.Korisnik.photoUrl, zzp.KorisnikId);
            return new HttpStatusCodeResult(HttpStatusCode.OK);

        }
    }
    }