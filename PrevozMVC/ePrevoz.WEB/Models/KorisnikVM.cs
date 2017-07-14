using ePrevoz.DATA.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Models
{
    public class KorisnikVM
    {
      

        public int Id { get; set; }
        public String UserId { get; set; }
        public bool IsDeleted { get; set; }
        public String ImePrezime { get; set; }
        public String Email { get; set; }
        public String photoUrl { get; set; }
        public String coverPhotoUrl { get; set; }
        public String FirebaseToken { get; set; }
         public int FirebaseTokenId { get; set; }
       public PrijateljiVM Prijatelj { get; set; }
        
      
    }
}