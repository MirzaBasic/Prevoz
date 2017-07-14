using ePrevoz.DATA.Model;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Runtime.Remoting.Contexts;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;

namespace ePrevoz.WEB.Data
{
   public class MojContext : DbContext { 
        public MojContext() : base("Name=PrevozConnectionString")
        {

    
        }
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Conventions.Remove<OneToManyCascadeDeleteConvention>();
            

        }
    

        public  DbSet<Prevoz> Prevozi { get; set; }

        public DbSet<Korisnik> Korisnici { get; set; }

        public DbSet<PrevoznoSredstvo> PrevoznaSredstva { get; set; }

        public DbSet<Stanice> Stanice { get; set; }
        public DbSet<FirebaseToken> FirebaseTokens { get; set; }
        public DbSet<Prijatelji> Prijatelji { get; set; }
        public DbSet<Poruka> Poruke { get; set; }

        public DbSet<ZahtjevZaPrevoz> ZahtjeviZaPrevoze { get; set; }
    }
}
