function selectAll(f,mode)
{
	if(mode==true)
	{
	    for(i=0;i<f.length;i++)
	    {
		    if(f.elements[i].type=="checkbox")
		    {
			    f.elements[i].checked=true;
		    }
	    }
	}
	else
	{
	    for(i=0;i<f.length;i++)
	    {
		    if(f.elements[i].type=="checkbox")
		    {
			    f.elements[i].checked=false;
		    }
	    }
	}
}


position = function(x,y)
{
    this.x = x;
    this.y = y;
}

getPosition = function(oElement)
{
    var objParent = oElement
    var oPosition = new position(0,0);
    while (objParent.tagName != "BODY")
    {
        oPosition.x += objParent.offsetLeft;
        oPosition.y += objParent.offsetTop;
        objParent = objParent.offsetParent;
    }
    return oPosition;
} 

function help(name,obj)
{
	 var  options={  
					   method:'get',  
					   parameters:"Action=ShowKey&KeyID="+escape(name),  
					   onComplete:function(transport)
						{  
							var returnvalue=transport.responseText;
							if (returnvalue.indexOf("??")>-1)
								showDiv(obj,'Error');
							else
								var tempstr=returnvalue;
								showDiv(obj,tempstr);
						}  
				   }; 
	new  Ajax.Request('HelpSearchAjax.aspx?no-cache='+Math.random(),options);
}


function showDiv(obj,content)
{
    var pos = getPosition(obj)
    var objDiv = document.createElement("div");
    objDiv.className="lionrong";//For IE
    objDiv.style.position = "absolute";
	var tempheight=pos.y;
	var tempwidth1,tempheight1;
	var windowwidth=document.body.clientWidth;
	
	var isIE5 = (navigator.appVersion.indexOf("MSIE 5")>0) || (navigator.appVersion.indexOf("MSIE")>0 && parseInt(navigator.appVersion)> 4);
	var isIE55 = (navigator.appVersion.indexOf("MSIE 5.5")>0);
	var isIE6 = (navigator.appVersion.indexOf("MSIE 6")>0);
	var isIE7 = (navigator.appVersion.indexOf("MSIE 7")>0);

	if(isIE5||isIE55||isIE6||isIE7){var tempwidth=pos.x+305;}else{var tempwidth=pos.x+312;}
	objDiv.style.width = "300px";
    objDiv.innerHTML = content;
	if (tempwidth>windowwidth)
	{
		tempwidth1=tempwidth-windowwidth
		objDiv.style.left = (pos.x-tempwidth1) + "px";
	}
	else
	{
		if(isIE5||isIE55||isIE6||isIE7){objDiv.style.left = (pos.x + 10) + "px";}else{objDiv.style.left = (pos.x) + "px";}
	}
	if(isIE5||isIE55||isIE6||isIE7){objDiv.style.top = (pos.y+16) + "px";}else{objDiv.style.top = (pos.y+16) + "px";}

    objDiv.style.display = "";
    document.onclick=function () { if(objDiv.style.display==""){objDiv.style.display="none";} }
    document.body.appendChild(objDiv);
}

