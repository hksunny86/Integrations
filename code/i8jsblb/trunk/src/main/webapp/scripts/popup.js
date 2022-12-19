function callLookup(src, popupName, width, height, dynamicPropertyString)
{
  left = (screen.width-width)/2;
  upper = (screen.height-height)/2;

  if(null != dynamicPropertyString && dynamicPropertyString != "")
  {
    window.open("popup.html?" + dynamicPropertyString + "&src="+src+"&popupName="+popupName,"popupWindow","width="+width+",height="+height+",menubar=no,toolbar=no,left="+left+",top="+upper+",directories=no,status=yes,scrollbars=yes,resizable=yes,status=no");
  }
  else
  {
    window.open("popup.html?src="+src+"&popupName="+popupName,"popupWindow","width="+width+",height="+height+",menubar=no,toolbar=no,left="+left+",top="+upper+",directories=no,status=yes,scrollbars=yes,resizable=yes,status=no");
  }
}

function getHashMapElement(key) // function for popup HashMap
{
  return this[key];
}

