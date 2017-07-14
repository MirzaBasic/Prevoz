using ePrevoz.DATA.Model;
using ePrevoz.WEB.Data;
using ePrevoz.WEB.Helper;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;
using System.Web.Mvc;
using System.Web.Script.Serialization;

namespace ePrevoz.WEB.Controllers
{
    public class MessageController : Controller
    {
        private static String FirebaseApiKey = "AIzaSyDms5HQKRUPokncqq5jm2t7xQtzwuzY64g";
        // GET: Message
        MojContext mc = new MojContext();


     




        public void Posalji(int type, int poslaoKorisnikid, String imeKorisnika, String photoUrl, int korisnikId)
        {


            List<FirebaseToken> ft = mc.FirebaseTokens.Where(x => x.KorisnikId == korisnikId).ToList();
            foreach (FirebaseToken token in ft)
            {

                PosaljiKorisnikuOdKorisnika(type, poslaoKorisnikid, imeKorisnika, photoUrl, token.Token);




            }



        }
        public void PosaljiNaTopic(int type, String imeKorisnika, String photoUrl, String topic)
        {


            WebRequest request = WebRequest.Create("https://fcm.googleapis.com/fcm/send");

            request.Method = "post";
            request.ContentType = "application/json";

            var message = new
            {

                data = new
                {
                    imeKorisnika = imeKorisnika,
                    type = type,
                    photoUrl = photoUrl,
                    korisnikId=0


                },
                to = "/topics/" + topic

            };
            var serializer = new JavaScriptSerializer();
            var json = serializer.Serialize(message);
            Byte[] byteArray = Encoding.UTF8.GetBytes(json);
            request.Headers.Add(string.Format("Authorization: key={0}", FirebaseApiKey));

            request.ContentLength = byteArray.Length;
            using (Stream dataStream = request.GetRequestStream())
            {
                dataStream.Write(byteArray, 0, byteArray.Length);
                using (WebResponse tResponse = request.GetResponse())
                {
                    using (Stream dataStreamResponse = tResponse.GetResponseStream())
                    {
                        using (StreamReader tReader = new StreamReader(dataStreamResponse))
                        {
                            String sResponseFromServer = tReader.ReadToEnd();
                            string str = sResponseFromServer;
                        }
                    }
                }
            }



        }

       

        public void PosaljiKorisnikuOdKorisnika(int type, int poslaoKorisnikid, String imeKorisnika, String photoUrl, String toUserTokenId)
        {



            WebRequest request = WebRequest.Create("https://fcm.googleapis.com/fcm/send");

            request.Method = "post";
            request.ContentType = "application/json";

            var message = new
            {

                data = new
                {
                    imeKorisnika = imeKorisnika,
                    type = type,
                    photoUrl=photoUrl,
                    korisnikId=poslaoKorisnikid


                },
                to = toUserTokenId
            
            };
            var serializer = new JavaScriptSerializer();
            var json = serializer.Serialize(message);
            Byte[] byteArray = Encoding.UTF8.GetBytes(json);
            request.Headers.Add(string.Format("Authorization: key={0}", FirebaseApiKey));

            request.ContentLength = byteArray.Length;
            using (Stream dataStream = request.GetRequestStream())
            {
                dataStream.Write(byteArray, 0, byteArray.Length);
                using (WebResponse tResponse = request.GetResponse())
                {
                    using (Stream dataStreamResponse = tResponse.GetResponseStream())
                    {
                        using (StreamReader tReader = new StreamReader(dataStreamResponse))
                        {
                            String sResponseFromServer = tReader.ReadToEnd();
                            string str = sResponseFromServer;
                        }
                    }
                }
            }
        }




    }

}
