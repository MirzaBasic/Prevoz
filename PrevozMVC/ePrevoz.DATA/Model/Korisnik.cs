using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{
    public class Korisnik:IEntity
    {
        public int Id { get; set; }
        public String UserId { get; set; }
        public bool IsDeleted { get; set; }
        public String ImePrezime { get; set; }
        public String  Email { get; set; }
        public String photoUrl { get; set; }
        public String coverPhotoUrl { get; set; }
    }
}
