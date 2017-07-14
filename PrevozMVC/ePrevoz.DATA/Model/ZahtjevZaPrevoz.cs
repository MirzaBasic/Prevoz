using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{
   public class ZahtjevZaPrevoz : IEntity
    {
        public int Id { get; set; }
        public bool IsDeleted { get; set; }

        public int KorisnikId { get; set; }

        public virtual Korisnik Korisnik { get; set; }

        public int Status { get; set; }

        public DateTime DatumKreiranja { get; set; }

        public int Kolicina { get; set; }

        public int PrevozId { get; set; }

        public virtual Prevoz Prevoz { get; set; }
    }
}
