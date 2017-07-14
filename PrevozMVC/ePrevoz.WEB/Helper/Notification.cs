using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Helper
{
    public class Notification
    {

       public static int NOVI_PREVOZ = 0;
       public static int ZAHTJEV_ZA_PRIJATELJSTVO = 1;
       public static int PRIHVACEN_ZAHTJEV_ZA_PRIJATELJSTVO = 2;
       public static int ZAHTJEV_ZA_PREVOZ = 3;
        public static int ODOBREN_ZAHTJEV_ZA_PREVOZ = 5;
        public static int ODBIJEN_ZAHTJEV_ZA_PREVOZ = 6;
        public static int NOVA_PORUKA = 4;
        public static String PREVOZI_TOPIC = "prevozi";
    }
}