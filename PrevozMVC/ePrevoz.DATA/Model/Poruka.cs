using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{

    
   public class Poruka :IEntity
    {
        public int Id { get; set; }
        public bool IsDeleted { get; set; }

        public string Text { get; set; }

        public byte[] Data { get; set; }

        public string DataType { get; set; }

        public virtual Korisnik KorisnikPoslao { get; set; }
        public virtual Korisnik KorisnikPrimio { get; set; }
        public int KorisnikPoslaoId { get; set; }
        public int KorisnikPrimioId { get; set; }

        public DateTime DatumKreiranja { get; set; }
        public int Status { get; set; }
    }
}
