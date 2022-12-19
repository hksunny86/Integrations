//Author: Naseer Ullah
function getSelectedItemValueOf(comboBoxId)
{
	var comboBox = document.getElementById( comboBoxId );
	var selctedItemVal = comboBox.options[comboBox.selectedIndex].value;
	return selctedItemVal;
}

function resetComboBox(comboBoxId)
{
	var comboBox = document.getElementById(comboBoxId);
	comboBox.options.length = 1;
}

function handleSenderDistChangeEvent()
{
	var senderDistributorIdCmb = document.getElementById('senderDistributorId');
	var senderDistributorIdVal = senderDistributorIdCmb.options[senderDistributorIdCmb.selectedIndex].value;

	var senderAgentRegionAsterisk = document.getElementById('senderAgentRegionAsterisk');
	var senderDistLevelIdAsterisk = document.getElementById('senderDistLevelIdAsterisk');
	if( "" == senderDistributorIdVal )
	{
		senderAgentRegionAsterisk.style.display='none';
		senderDistLevelIdAsterisk.style.display='none';
	}
	else
	{
		senderAgentRegionAsterisk.style.display='inline';
		senderDistLevelIdAsterisk.style.display='inline';
	}
	return true;
}

function handleReceiverDistChangeEvent()
{
	var receiverDistributorIdCmb = document.getElementById('receiverDistributorId');
	var receiverDistributorIdVal = receiverDistributorIdCmb.options[receiverDistributorIdCmb.selectedIndex].value;

	var receiverAgentRegionAsterisk = document.getElementById('receiverAgentRegionAsterisk');
	var receiverDistLevelIdAsterisk = document.getElementById('receiverDistLevelIdAsterisk');
	if( "" == receiverDistributorIdVal )
	{
		receiverAgentRegionAsterisk.style.display='none';
		receiverDistLevelIdAsterisk.style.display='none';
	}
	else
	{
		receiverAgentRegionAsterisk.style.display='inline';
		receiverDistLevelIdAsterisk.style.display='inline';
	}
	return true;
}

function handlePreSenderDistOnChange()
{
	resetComboBox('senderDistLevelId' );
	return handleSenderDistChangeEvent();
}

function handlePreReceiverDistOnChange()
{
	resetComboBox('receiverDistLevelId' );
	return handleReceiverDistChangeEvent();
}