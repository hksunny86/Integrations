// javascript for generic select order shuttle Control
// Author Rizwan-ur-Rehman


function deleteOption(fromObject,index)
{
  fromObject.options[index] = null;
}



function addOption(toObject,text,value)
{
  var defaultSelected = false;
  var selected = false;
  var optionName = new Option(text, value, defaultSelected, selected)
  toObject.options[toObject.length] = optionName;
}



function copySelected(fromObject,toObject)
{
  for(var i=0, len=fromObject.options.length;i<len;i++)
  {
    if(fromObject.options[i].selected)
    {
      addOption(toObject,fromObject.options[i].text,fromObject.options[i].value);
    }
  }
  for (var i=fromObject.options.length-1;i>-1;i--)
  {
    if (null != fromObject.options[i].selected && fromObject.options[i].selected != undefined && fromObject.options[i].selected)
    {
      deleteOption(fromObject,i);
    }
  }
}




function copyAll(fromObject,toObject)
{
  for (var i=0, l=fromObject.options.length;i<l;i++)
  {
    addOption(toObject,fromObject.options[i].text,fromObject.options[i].value);
  }
  for (var i=fromObject.options.length-1;i>-1;i--)
  {
    deleteOption(fromObject,i);
  }
}



function moveUp(destObject)
{
  for(var i=0, len=destObject.options.length;i<len;i++)
  {
    if(destObject.options[i].selected)
    {
      if(i != 0)
      {
        swap(destObject,i,i-1);
        destObject.options[i].selected = false;
      }
    }
  }
}


function swap(destObject,currentIndex,swappingIndex)
{
  var selected = false;
  var defaultSelected = false;
  var tempOption = new Option(destObject.options[currentIndex].text, destObject.options[currentIndex].value, defaultSelected, selected);
  destObject.options[currentIndex].text=destObject.options[swappingIndex].text;
  destObject.options[currentIndex].value=destObject.options[swappingIndex].value;
  destObject.options[swappingIndex].text=tempOption.text;
  destObject.options[swappingIndex].value=tempOption.value;
}



function moveDown(destObject)
{
  for(var i=0, len=destObject.options.length;i<len;i++)
  {
    if(destObject.options[i].selected)
    {
      if(i != len)
      {
        swap(destObject,i,i+1);
        destObject.options[i].selected = false;
      }
    }
  }
}



function moveTop(destObject)
{
  var selectedIndex;
  for(var i=0, len=destObject.options.length;i<len;i++)
  {
    if(destObject.options[i].selected)
    {
      selectedIndex = i;
      while(i != 0)
      {
        swap(destObject,i,i-1);
        i--;
      }
      destObject.options[selectedIndex].selected = false;
      break;
    }
  }
}



function moveButtom(destObject)
{
  var selectedIndex;
  for(var i=0, len=destObject.options.length;i<len;i++)
  {
    if(destObject.options[i].selected)
    {
      selectedIndex = i;
      while(i != len-1)
      {
        swap(destObject,i,i+1);
        i++;
      }
      destObject.options[selectedIndex].selected = false;
      break;
    }
  }
}




