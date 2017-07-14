namespace ePrevoz.WEB.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class zahtjevZaPrevoz_dodata_Kolicina : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.ZahtjevZaPrevozs", "Kolicina", c => c.Int(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.ZahtjevZaPrevozs", "Kolicina");
        }
    }
}
