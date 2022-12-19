
/*
* This method clears the values of Extrem Table Parameters. Helpfull in Form
* Base Search Cases. Specially it solves the Export issue (first exporting and
* then again pressing search results in export wrongly.
*/
function clearEcParameters(theForm)
  {
    theForm.ec_p.value="";
    theForm.ec_ev.value="";
    theForm.ec_efn.value="";
  }

