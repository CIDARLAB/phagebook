//!-- <SCRIPT LANGUAGE = "javascript"> -->


function OnChange(dropdown)
{
	var myIndex = dropdown.selectedIndex
	var SelValue = dropdown.options[myindex].SelValue
	if (SelValue == "Kathleen Lewis")
	{
		document.getElementById('notebookID').disabled = "";
	}
	else
	{
		document.getElementById('notebookID').disabled = "disabled";
	}
}