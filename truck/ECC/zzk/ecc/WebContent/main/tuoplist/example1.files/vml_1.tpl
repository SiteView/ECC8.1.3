<html xmlns:v="urn:schemas-microsoft-com:vml" 
 xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="vs_targetSchema" content="http://schemas.microsoft.com/intellisense/ie5"/>
<link rel="stylesheet" type="text/css" href="visio.css"/>
<title></title>
<style type="text/css">
	 v\:* { behavior: url(#default#VML); }
</style>

<script src="vml_1.js" type="text/jscript" language="jscript"></script>

<script type="text/jscript" language="jscript">

	var pageID = 4;
	var viewMgr = null;

	if (parent.g_FirstPageToLoad != null && parent.g_FirstPageToLoad.length > 0)
	{
		if (parent.g_FileList[parent.g_CurPageIndex].PageID != pageID)
		{
			location.replace(parent.g_FileList[parent.g_CurPageIndex].PriImage);
		}

		parent.g_FirstPageToLoad = null;
	}

	function getPNZ()
	{
		var rawHTML = VMLDiv.innerHTML;
		var strReturn = ""
		
		strReturn = rawHTML.slice( rawHTML.indexOf( '<v:group' ), rawHTML.indexOf( "</v:shape>" ) );
		strReturn += "</v:shape></v:group>\n";
				
		return strReturn;
	}
	
	function load()
	{
		viewMgr = new parent.CViewMgr("ConvertedImage", "arrowDiv");

		viewMgr.put_Location = ViewMgrSetVMLLocation;


		viewMgr.visBBoxLeft = 0.155814;
		viewMgr.visBBoxRight = 11.970171;
		viewMgr.visBBoxBottom = 0.155814;
		viewMgr.visBBoxTop = 8.426864;

		viewMgr.Zoom = VMLZoomChange;
		viewMgr.setView= VMLSetView;

		viewMgr.SupportsDetails = true;
		viewMgr.SupportsSearch = true;

		parent.viewMgr = viewMgr;

		fit();
	}

	function unload()
	{
		viewMgr = null;
		parent.viewMgr = null;
	}

	function fit()
	{
		if(parent.frmToolbar)
		{
			if (parent.g_WidgetsLoaded)
			{
				var zoom100 = parent.frmToolbar.document.all('a100');
				if (zoom100)
				{
					parent.viewMgr.PostZoomProcessing = PostZoomProcessing;
					zoom100.click();
				}
				else
				{
					parent.viewMgr.PostZoomProcessing = PostZoomProcessing;
					viewMgr.Zoom(100);
				}
			}
			else
			{
				window.setTimeout("fit()", 500);
			}
		}	
		else
		{
			parent.viewMgr.PostZoomProcessing = PostZoomProcessing;
			viewMgr.Zoom(100);
		}
	}

	function PostZoomProcessing (newZoomLevel)
	{
		parent.viewMgr.PostZoomProcessing = null;
		var pageIndex = parent.PageIndexFromID (pageID);
		parent.viewMgr.getPNZ = getPNZ;
		parent.CurPageUpdate (pageIndex);
	}


	
	var isUpLevel = parent.isUpLevel;
	var OnShapeClick = parent.OnShapeClick;
	var OnShapeKey = parent.OnShapeKey;
	var UpdateTooltip = parent.UpdateTooltip;
	var clickMenu = parent.clickMenu;
	var toggleMenuDiv = parent.toggleMenuDiv;
	var toggleMenuLink = parent.toggleMenuLink;
	var GoToPage = parent.GoToPage;

	window.onload = load;
	window.onunload = unload;
	document.onclick = clickMenu;




</script>

</head>

<body style="MARGIN:10px" onresize="VMLOnResize();" onscroll="VMLOnScroll();">

<div id="arrowdiv" style="position:absolute;top:0;left:0;visibility:hidden;z-index:5">
<img id="arrowgif" alt="显示所选形状的位置" src="arrow.gif"/>
</div>

<div id="menu1" onclick="clickMenu()" class="hlMenu">
</div>




<div id="VMLDiv" >


<v:group  id="ConvertedImage" style="width:5.713595in;height:4.000000in;position:absolute;visibility=hidden;" coordSize="2857,2000" coordOrigin="-1000,-1000" >

<v:shapetype 
  id="VISSHAPE"
  target="_parent"
  coordsize="2857,2000"
  coordorigin="-1000,-1000"
  stroked="f"
  strokecolor="red"
  filled="t"
>
<v:fill opacity="0.0"/>
</v:shapetype>


<v:shape style="top:-1000.0;left:-1000.0;width:2856.8;height:2000.0;position:absolute" coordSize="2857,2000" coordOrigin="-1000,-1000" >
<v:imagedata src="vml_1.emz"/>
</v:shape>

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 119,154 l -98,154 l -110,154 l -110,175 l -98,175 l 119,175 l 131,175 l 131,154 l 119,154xe" onmouseover="UpdateTooltip(this,4,1169)" onclick="OnShapeClick(4,1169)" onfocus="UpdateTooltip(this,4,1169);" onkeyup="OnShapeKey(4,1169)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_IP:192.168.0.50+color" tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 43,155 l 43,151 l 58,151 l 58,47 l 58,-15 l 33,-15 l -37,-15 l -37,57 l -37,151 l -22,151 l -22,155 l -37,155 l -37,156 l 58,156 l 58,155 l 43,155xe" onmouseover="UpdateTooltip(this,4,1180)" onclick="OnShapeClick(4,1180)" onfocus="UpdateTooltip(this,4,1180);" onkeyup="OnShapeKey(4,1180)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 624,-97 l 407,-97 l 395,-97 l 395,-76 l 407,-76 l 624,-76 l 636,-76 l 636,-97 l 624,-97xe" onmouseover="UpdateTooltip(this,4,1320)" onclick="OnShapeClick(4,1320)" onfocus="UpdateTooltip(this,4,1320);" onkeyup="OnShapeKey(4,1320)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 879,7 l 661,7 l 650,7 l 650,28 l 661,28 l 879,28 l 890,28 l 890,7 l 879,7xe" onmouseover="UpdateTooltip(this,4,1331)" onclick="OnShapeClick(4,1331)" onfocus="UpdateTooltip(this,4,1331);" onkeyup="OnShapeKey(4,1331)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 119,-57 l -98,-57 l -110,-57 l -110,-36 l -98,-36 l 119,-36 l 131,-36 l 131,-57 l 119,-57xe" onmouseover="UpdateTooltip(this,4,1029)" onclick="OnShapeClick(4,1029)" onfocus="UpdateTooltip(this,4,1029);" onkeyup="OnShapeKey(4,1029)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_IP:10.0.170.17+color" tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 550,534 l 550,531 l 566,531 l 566,427 l 566,365 l 539,365 l 462,365 l 462,437 l 462,531 l 478,531 l 478,534 l 462,534 l 462,536 l 566,536 l 566,534 l 550,534xe" onmouseover="UpdateTooltip(this,4,1133)" onclick="OnShapeClick(4,1133)" onfocus="UpdateTooltip(this,4,1133);" onkeyup="OnShapeKey(4,1133)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 879,196 l 661,196 l 650,196 l 650,217 l 661,217 l 879,217 l 890,217 l 890,196 l 879,196xe" onmouseover="UpdateTooltip(this,4,1214)" onclick="OnShapeClick(4,1214)" onfocus="UpdateTooltip(this,4,1214);" onkeyup="OnShapeKey(4,1214)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1132,91 l 915,91 l 903,91 l 903,112 l 915,112 l 1132,112 l 1144,112 l 1144,91 l 1132,91xe" onmouseover="UpdateTooltip(this,4,1345)" onclick="OnShapeClick(4,1345)" onfocus="UpdateTooltip(this,4,1345);" onkeyup="OnShapeKey(4,1345)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1385,302 l 1168,302 l 1156,302 l 1156,323 l 1168,323 l 1385,323 l 1397,323 l 1397,302 l 1385,302xe" onmouseover="UpdateTooltip(this,4,1638)" onclick="OnShapeClick(4,1638)" onfocus="UpdateTooltip(this,4,1638);" onkeyup="OnShapeKey(4,1638)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_Entity:SQL%202005+color" tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m -435,529 l -435,446 l -435,431 l -435,431 l -435,363 l -559,363 l -559,388 l -559,395 l -559,399 l -559,406 l -559,410 l -559,416 l -559,421 l -559,427 l -559,431 l -559,431 l -559,529 l -559,536 l -435,536 l -435,529xe" onmouseover="UpdateTooltip(this,4,941)" onclick="OnShapeClick(4,941)" onfocus="UpdateTooltip(this,4,941);" onkeyup="OnShapeKey(4,941)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 626,112 l 408,112 l 397,112 l 397,133 l 408,133 l 626,133 l 637,133 l 637,112 l 626,112xe" onmouseover="UpdateTooltip(this,4,182)" onclick="OnShapeClick(4,182)" onfocus="UpdateTooltip(this,4,182);" onkeyup="OnShapeKey(4,182)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m -864,569 l -634,569 l -622,569 l -622,-285 l -634,-285 l -864,-285 l -876,-285 l -876,569 l -864,569x m -640,533 l -857,533 l -857,-266 l -640,-266 l -640,533xe" onmouseover="UpdateTooltip(this,4,176)" onclick="OnShapeClick(4,176)" onfocus="UpdateTooltip(this,4,176);" onkeyup="OnShapeKey(4,176)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m -611,569 l -381,569 l -369,569 l -369,-285 l -381,-285 l -611,-285 l -622,-285 l -622,569 l -611,569x m -387,533 l -604,533 l -604,-266 l -387,-266 l -387,533xe" onmouseover="UpdateTooltip(this,4,1845)" onclick="OnShapeClick(4,1845)" onfocus="UpdateTooltip(this,4,1845);" onkeyup="OnShapeKey(4,1845)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m -357,569 l -128,569 l -116,569 l -116,-285 l -128,-285 l -357,-285 l -369,-285 l -369,569 l -357,569x m -134,533 l -351,533 l -351,-266 l -134,-266 l -134,533xe" onmouseover="UpdateTooltip(this,4,1851)" onclick="OnShapeClick(4,1851)" onfocus="UpdateTooltip(this,4,1851);" onkeyup="OnShapeKey(4,1851)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m -104,569 l 125,569 l 137,569 l 137,-285 l 125,-285 l -104,-285 l -116,-285 l -116,569 l -104,569x m 119,533 l -98,533 l -98,-266 l 119,-266 l 119,533xe" onmouseover="UpdateTooltip(this,4,1857)" onclick="OnShapeClick(4,1857)" onfocus="UpdateTooltip(this,4,1857);" onkeyup="OnShapeKey(4,1857)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 149,569 l 379,569 l 390,569 l 390,-285 l 379,-285 l 149,-285 l 137,-285 l 137,569 l 149,569x m 372,533 l 155,533 l 155,-266 l 372,-266 l 372,533xe" onmouseover="UpdateTooltip(this,4,1833)" onclick="OnShapeClick(4,1833)" onfocus="UpdateTooltip(this,4,1833);" onkeyup="OnShapeKey(4,1833)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 402,569 l 632,569 l 644,569 l 644,-285 l 632,-285 l 402,-285 l 390,-285 l 390,569 l 402,569x m 626,533 l 409,533 l 409,-266 l 626,-266 l 626,533xe" onmouseover="UpdateTooltip(this,4,1296)" onclick="OnShapeClick(4,1296)" onfocus="UpdateTooltip(this,4,1296);" onkeyup="OnShapeKey(4,1296)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 655,569 l 885,569 l 897,569 l 897,-285 l 885,-285 l 655,-285 l 644,-285 l 644,569 l 655,569x m 879,533 l 662,533 l 662,-266 l 879,-266 l 879,533xe" onmouseover="UpdateTooltip(this,4,1839)" onclick="OnShapeClick(4,1839)" onfocus="UpdateTooltip(this,4,1839);" onkeyup="OnShapeKey(4,1839)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 909,569 l 1138,569 l 1150,569 l 1150,-285 l 1138,-285 l 909,-285 l 897,-285 l 897,569 l 909,569x m 1132,533 l 915,533 l 915,-266 l 1132,-266 l 1132,533xe" onmouseover="UpdateTooltip(this,4,1863)" onclick="OnShapeClick(4,1863)" onfocus="UpdateTooltip(this,4,1863);" onkeyup="OnShapeKey(4,1863)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1162,569 l 1391,569 l 1403,569 l 1403,-285 l 1391,-285 l 1162,-285 l 1150,-285 l 1150,569 l 1162,569x m 1385,533 l 1168,533 l 1168,-266 l 1385,-266 l 1385,533xe" onmouseover="UpdateTooltip(this,4,1869)" onclick="OnShapeClick(4,1869)" onfocus="UpdateTooltip(this,4,1869);" onkeyup="OnShapeKey(4,1869)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1410,-57 l 1649,-57 l 1649,-268 l 1410,-268 l 1410,-57xe" onmouseover="UpdateTooltip(this,4,2111)" onclick="OnShapeClick(4,2111)" onfocus="UpdateTooltip(this,4,2111);" onkeyup="OnShapeKey(4,2111)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1410,175 l 1649,175 l 1649,-57 l 1410,-57 l 1410,175xe" onmouseover="UpdateTooltip(this,4,2128)" onclick="OnShapeClick(4,2128)" onfocus="UpdateTooltip(this,4,2128);" onkeyup="OnShapeKey(4,2128)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1410,533 l 1649,533 l 1649,175 l 1410,175 l 1410,533xe" onmouseover="UpdateTooltip(this,4,2145)" onclick="OnShapeClick(4,2145)" onfocus="UpdateTooltip(this,4,2145);" onkeyup="OnShapeKey(4,2145)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2857;height:2000;position:absolute" path=" m 1415,569 l 1645,569 l 1656,569 l 1656,-285 l 1645,-285 l 1415,-285 l 1403,-285 l 1403,569 l 1415,569x m 1638,533 l 1421,533 l 1421,-266 l 1638,-266 l 1638,533xe" onmouseover="UpdateTooltip(this,4,1875)" onclick="OnShapeClick(4,1875)" onfocus="UpdateTooltip(this,4,1875);" onkeyup="OnShapeKey(4,1875)"/> 

</v:group>



</div>



</body>
</html>

<script>
function reloadrefresh()
{
parent.location.reload();window.setTimeout("reloadrefresh()", 180000);
}
window.setTimeout("reloadrefresh()", 180000)
var date = new Date(); 
var msg = "The Last Refresh Time : " + date.toLocaleDateString() + date.toLocaleTimeString();   
function scroll(seed) 
{ 
var out = " ";    
var c = 1; 
if (seed > 100) { 
seed--; 
var cmd="scroll(" + seed + ")"; 
timerTwo=window.setTimeout(cmd,100); 
} 
else if (seed <= 100 && seed > 0) { 
for (c=0 ; c < seed ; c++) { 
out+=" "; 
} 
out+=msg; 
seed--; 
var cmd="scroll(" + seed + ")"; 
window.status=out;
timerTwo=window.setTimeout(cmd,100);
} else if (seed <= 0) { 
if (-seed < msg.length) { 
out+=msg.substring(-seed,msg.length); 
seed--; 
var cmd="scroll(" + seed + ")"; 
window.status=out;
timerTwo=window.setTimeout(cmd,100); 
} 
else { 
window.status=" ";
timerTwo=window.setTimeout("scroll(100)",7); 
} 
} 
} 
timerONE=window.setTimeout('scroll(100)',50); 
</script>
*#siteview7endflag#*
