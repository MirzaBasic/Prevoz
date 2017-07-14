using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{
    public class FirebaseToken : IEntity
    {
        public int Id { get; set; }
        public bool IsDeleted { get; set; }
        public String Token { get; set; }
        public virtual Korisnik Korisnik { get; set; }
        public int KorisnikId { get; set; }
    }
}
