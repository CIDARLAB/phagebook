

function notebook_projects(dropdown)
{
	var myIndex = dropdown.selectedIndex
	var SelValue = dropdown.options[myindex].SelValue
	if (SelValue == "Kathleen Lewis")
	{
		document.getElementById('notebookID').disabled = false;
	}
	else
	{
		document.getElementById('notebookID').disabled = true;
	}
};
