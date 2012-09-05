function showHideDiv(divId) {
	var e, v, ow, oh;
	
	if(document.getElementById) {
		e = document.getElementById(divId);
	} else if(document.all) {
		e = document.all[divId];
	} else if(document.layers) {
		e = document.layers[divId];
	}

	v = e.style;
	ow = e.offsetWidth;
	oh = e.offsetHeight;
	if((v.display == '') && (ow != undefined) && (oh != undefined)) {
		v.display = ((ow != 0) && (oh != 0)) ? 'block' : 'none';
	}

	v.display = (v.display == 'block') ? 'none' : 'block';	
}

function swapDiv(divId1, divId2) {
	var e1, e2, v1, v2, ow1, oh1;
	
	if(document.getElementById) {
		e1 = document.getElementById(divId1);
	} else if(document.all) {
		e1 = document.all[divId1];
	} else if(document.layers) {
		e1 = document.layers[divId1];
	}
	v1 = e1.style;

	if(document.getElementById) {
		e2 = document.getElementById(divId2);
	} else if(document.all) {
		e2 = document.all[divId2];
	} else if(document.layers) {
		e2 = document.layers[divId2];
	}
	v2 = e2.style;
	
	ow1 = e1.offsetWidth;
	oh1 = e1.offsetHeight;
	if((v1.display == '') && (ow1 != undefined) && (oh1 != undefined)) {
		v1.display = ((ow1 != 0) && (oh1 != 0)) ? 'block' : 'none';
	}

	v1.display = (v1.display == 'block') ? 'none' : 'block';
	v2.display = (v1.display == 'block') ? 'none' : 'block';
}

var deleteWasClicked = false;
var deleteWasConfirmed = false;

function validate(form) {
	if (deleteWasClicked && !deleteWasConfirmed) {
		deleteWasClicked = false;
		return false;
	}
	
	form.submit();
}
