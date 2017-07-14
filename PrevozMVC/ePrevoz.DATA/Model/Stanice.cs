using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{
    public class Stanice : IEntity
    {
        public int Id { get; set; }
        public bool IsDeleted { get; set; }
        public string Grad { get; set; }
        public double Lat { get; set; }
        public double Lng { get; set; }
        public virtual Prevoz Prevoz { get; set; }
        public int RednaOdznaka { get; set; }

        public int PrevozId { get; set; }

    }
}
