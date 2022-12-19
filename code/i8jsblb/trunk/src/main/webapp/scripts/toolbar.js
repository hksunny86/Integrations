// javascript for generic toolbar handling
// Author Maqsood Shahzad


function onSave(theForm,saveTarget)
{

//alert('Inside onSave');

  if(theForm.onsubmit != undefined)
  {
    if(!theForm.onsubmit())
    {
  //  alert('Onsubmit was not there returning');
      return;
    }
  }

if(saveTarget != null)
{
    //alert('save target was not null');
  theForm.action = saveTarget;
}
else
{
    //alert('SaveTraget was null....setting the form action');
theForm.action = theForm.action+"?_save=Submit";
}
//alert('submitting the form the action is '+ theForm.action);
  theForm.submit();

}

function onDelete(theForm,deleteTarget)
{
  if(deleteTarget != null)
  {
    theForm.action = deleteTarget;
  }
  else
  {
  theForm.action = theForm.action+"?_delete=Delete";

  }
  theForm.submit();
}
function onCancel(theForm,cancelTarget)
{


  if(cancelTarget != null)
  {
    theForm.action = cancelTarget;
  }
  else
  {
  theForm.action = theForm.action+"?_cancel=cancel";

  }
    theForm.submit();

}
