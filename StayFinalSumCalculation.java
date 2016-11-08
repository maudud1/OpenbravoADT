 package com.rawntech.hotelmanagement.ad_callouts;

import com.rawntech.hotelmanagement.data.HotelRoom;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.ServletException;
 
import org.openbravo.utils.FormatUtilities;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.base.secureApp.VariablesSecureApp;
import org.openbravo.dal.service.OBDal;
import java.math.BigDecimal;

public class StayFinalSumCalculation extends SimpleCallout{



@Override
protected void execute(CalloutInfo info) throws ServletException {


  String strDateIn = info.getStringParameter("inpdateIn", null);
  String strDateOut = info.getStringParameter("inpdateOut", null);
  String strRoomRate = info.getStringParameter("inproomRate", null);
  String strRoomId = info.getStringParameter("inphotelRoomId", null);

       if (!strDateIn.equals("") && !strDateOut.equals("")
              && !strRoomRate.equals("") && !strRoomId.equals("")) {
          info.addResult("inpfinalSum", getFinalSum(info.vars, strDateIn, strDateOut, strRoomRate, strRoomId));
      }
  }


protected BigDecimal getFinalSum(VariablesSecureApp vars,
          String strDateIn, String strDateOut, String strRoomRate,
          String strRoomId) { 



  final HotelRoom hotelRoom = OBDal.getInstance().get(HotelRoom.class, strRoomId);
    // Session variable deriving from a setting in Openbravo.properties
  DateFormat formatter = new SimpleDateFormat(vars.getSessionValue("#AD_JavaDateFormat")); 
  Date dateIn;
  Date dateOut;
  try {

    dateIn = (Date) formatter.parse(strDateIn);
    dateOut = (Date) formatter.parse(strDateOut);
    } catch (Exception e) {
        dateIn = new Date();
        dateOut = new Date();
    }

final long longDays = (dateOut.getTime() - dateIn.getTime())/(24*60*60*1000);

  
  // retrieve correct room rate
  BigDecimal rate = new BigDecimal(0);    
  if (strRoomRate.equals("A"))
    rate = hotelRoom.getArate();
  else if (strRoomRate.equals("B"))
    rate = hotelRoom.getBrate();
  else
    rate = hotelRoom.getCrate();
 return rate.multiply(new BigDecimal(longDays));


  }
}
