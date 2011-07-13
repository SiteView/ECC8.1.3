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

	var pageID = 0;
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


		viewMgr.visBBoxLeft = 1.764162;
		viewMgr.visBBoxRight = 7.122807;
		viewMgr.visBBoxBottom = 7.637795;
		viewMgr.visBBoxTop = 10.195006;

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


<v:group  id="ConvertedImage" style="width:8.382017in;height:4.000000in;position:absolute;visibility=hidden;" coordSize="4191,2000" coordOrigin="-1000,-1000" >

<v:shapetype 
  id="VISSHAPE"
  target="_parent"
  coordsize="4191,2000"
  coordorigin="-1000,-1000"
  stroked="f"
  strokecolor="red"
  filled="t"
>
<v:fill opacity="0.0"/>
</v:shapetype>


<v:shape style="top:-1000.0;left:-1000.0;width:4191.0;height:2000.0;position:absolute" coordSize="4191,2000" coordOrigin="-1000,-1000" >
<v:imagedata src="vml_1.emz"/>
</v:shape>

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m -691,-868 l 1513,-868 l 1513,-988 l -691,-988 l -691,-868xe" onmouseover="UpdateTooltip(this,0,151)" onclick="OnShapeClick(0,151)" onfocus="UpdateTooltip(this,0,151);" onkeyup="OnShapeKey(0,151)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m -584,988 l -125,988 l -125,319 l -584,319 l -584,988xe" onmouseover="UpdateTooltip(this,0,181)" onclick="OnShapeClick(0,181)" onfocus="UpdateTooltip(this,0,181);" onkeyup="OnShapeKey(0,181)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m -735,86 l -110,86 l -110,-173 l -735,-173 l -735,86xe" onmouseover="UpdateTooltip(this,0,183)" onclick="OnShapeClick(0,183)" onfocus="UpdateTooltip(this,0,183);" onkeyup="OnShapeKey(0,183)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 772,75 l 1397,75 l 1397,-184 l 772,-184 l 772,75xe" onmouseover="UpdateTooltip(this,0,160)" onclick="OnShapeClick(0,160)" onfocus="UpdateTooltip(this,0,160);" onkeyup="OnShapeKey(0,160)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 3059,-421 l 3059,-421 l 3059,-421 l 2927,-493 l 2845,-449 l 2845,-449 l 2845,-268 l 2845,-268 l 2849,-266 l 2978,-197 l 2978,-197 l 2983,-198 l 2987,-199 l 2992,-200 l 2997,-201 l 3002,-202 l 3006,-204 l 3011,-206 l 3016,-208 l 3020,-210 l 3024,-212 l 3029,-214 l 3033,-217 l 3037,-219 l 3041,-222 l 3045,-225 l 3049,-228 l 3052,-231 l 3056,-234 l 3059,-238 l 3059,-238 l 3059,-421 l 3059,-421 l 3059,-421xe" onmouseover="UpdateTooltip(this,0,171)" onclick="OnShapeClick(0,171)" onfocus="UpdateTooltip(this,0,171);" onkeyup="OnShapeKey(0,171)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 2978,-377 l 2845,-449 l 2927,-493 l 3059,-421 l 3056,-418 l 3052,-414 l 3049,-411 l 3045,-408 l 3041,-405 l 3037,-402 l 3033,-399 l 3029,-396 l 3025,-394 l 3020,-391 l 3016,-389 l 3011,-387 l 3007,-385 l 3002,-383 l 2997,-382 l 2992,-380 l 2988,-379 l 2983,-378 l 2978,-377 l 2978,-377 l 2980,-367 l 2983,-368 l 2986,-368 l 2989,-369 l 2992,-370 l 2996,-371 l 2999,-372 l 3002,-373 l 3005,-374 l 3008,-375 l 3011,-376 l 3015,-378 l 3018,-379 l 3021,-380 l 3024,-382 l 3026,-383 l 3029,-385 l 3031,-386 l 3035,-388 l 3038,-390 l 3041,-392 l 3043,-394 l 3046,-396 l 3048,-397 l 3051,-399 l 3053,-401 l 3055,-403 l 3057,-405 l 3060,-407 l 3062,-409 l 3064,-412 l 3066,-414 l 3067,-414 l 2932,-501 l 2840,-457 l 2973,-368xe" onmouseover="UpdateTooltip(this,0,171)" onclick="OnShapeClick(0,171)" onfocus="UpdateTooltip(this,0,171);" onkeyup="OnShapeKey(0,171)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 2978,-377 l 2983,-378 l 2988,-379 l 2992,-380 l 2997,-382 l 3002,-383 l 3007,-385 l 3011,-387 l 3016,-389 l 3020,-391 l 3025,-394 l 3029,-396 l 3033,-399 l 3037,-402 l 3041,-405 l 3045,-408 l 3049,-411 l 3052,-414 l 3056,-418 l 3059,-421 l 3059,-238 l 3056,-234 l 3052,-231 l 3049,-228 l 3045,-225 l 3041,-222 l 3037,-219 l 3033,-217 l 3029,-214 l 3024,-212 l 3020,-210 l 3016,-208 l 3011,-206 l 3006,-204 l 3002,-202 l 2997,-201 l 2992,-200 l 2987,-199 l 2983,-198 l 2978,-197 l 2978,-377 l 2978,-377 l 2968,-377 l 2968,-197 l 2982,-187 l 2985,-188 l 2989,-189 l 2992,-189 l 2995,-190 l 2998,-191 l 3001,-192 l 3005,-193 l 3008,-194 l 3011,-195 l 3014,-196 l 3017,-198 l 3020,-199 l 3023,-200 l 3026,-201 l 3028,-203 l 3031,-204 l 3034,-206 l 3037,-208 l 3040,-210 l 3043,-211 l 3045,-213 l 3048,-215 l 3050,-217 l 3053,-218 l 3055,-220 l 3057,-222 l 3059,-224 l 3062,-226 l 3064,-228 l 3066,-231 l 3066,-231 l 3069,-421 l 3050,-426 l 3048,-424 l 3046,-422 l 3044,-420 l 3042,-418 l 3040,-416 l 3037,-414 l 3035,-413 l 3032,-411 l 3030,-409 l 3027,-407 l 3024,-405 l 3022,-404 l 3019,-402 l 3017,-401 l 3014,-399 l 3012,-398 l 3009,-397 l 3006,-396 l 3003,-394 l 3000,-393 l 2997,-392 l 2994,-391 l 2991,-390 l 2988,-389 l 2986,-389 l 2983,-388 l 2980,-387 l 2977,-387 l 2976,-387xe" onmouseover="UpdateTooltip(this,0,171)" onclick="OnShapeClick(0,171)" onfocus="UpdateTooltip(this,0,171);" onkeyup="OnShapeKey(0,171)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 3059,-179 l 3059,-180 l 3059,-180 l 2927,-251 l 2845,-207 l 2845,-207 l 2845,-26 l 2845,-26 l 2849,-24 l 2978,45 l 2978,45 l 2983,44 l 2987,43 l 2992,42 l 2997,41 l 3002,39 l 3006,38 l 3011,36 l 3016,34 l 3020,32 l 3024,30 l 3029,27 l 3033,25 l 3037,22 l 3041,20 l 3045,17 l 3049,14 l 3052,11 l 3056,7 l 3059,4 l 3059,4 l 3059,-179 l 3059,-179 l 3059,-179xe" onmouseover="UpdateTooltip(this,0,192)" onclick="OnShapeClick(0,192)" onfocus="UpdateTooltip(this,0,192);" onkeyup="OnShapeKey(0,192)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 2978,-135 l 2845,-207 l 2927,-251 l 3059,-179 l 3056,-176 l 3052,-172 l 3049,-169 l 3045,-166 l 3041,-163 l 3037,-160 l 3033,-157 l 3029,-155 l 3025,-152 l 3020,-150 l 3016,-147 l 3011,-145 l 3007,-143 l 3002,-142 l 2997,-140 l 2992,-139 l 2988,-137 l 2983,-136 l 2978,-135 l 2978,-135 l 2980,-125 l 2983,-126 l 2986,-127 l 2989,-127 l 2992,-128 l 2996,-129 l 2999,-130 l 3002,-131 l 3005,-132 l 3008,-133 l 3011,-134 l 3015,-136 l 3018,-137 l 3021,-139 l 3024,-140 l 3026,-141 l 3029,-143 l 3031,-144 l 3035,-146 l 3038,-148 l 3041,-150 l 3043,-152 l 3046,-154 l 3048,-156 l 3051,-158 l 3053,-160 l 3055,-162 l 3057,-164 l 3060,-166 l 3062,-168 l 3064,-170 l 3066,-172 l 3067,-173 l 2932,-260 l 2840,-216 l 2973,-126xe" onmouseover="UpdateTooltip(this,0,192)" onclick="OnShapeClick(0,192)" onfocus="UpdateTooltip(this,0,192);" onkeyup="OnShapeKey(0,192)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 2978,-135 l 2983,-136 l 2988,-137 l 2992,-139 l 2997,-140 l 3002,-142 l 3007,-143 l 3011,-145 l 3016,-147 l 3020,-150 l 3025,-152 l 3029,-155 l 3033,-157 l 3037,-160 l 3041,-163 l 3045,-166 l 3049,-169 l 3052,-173 l 3056,-176 l 3059,-180 l 3059,4 l 3056,7 l 3052,11 l 3049,14 l 3045,17 l 3041,20 l 3037,22 l 3033,25 l 3029,27 l 3024,30 l 3020,32 l 3016,34 l 3011,36 l 3006,38 l 3002,39 l 2997,41 l 2992,42 l 2987,43 l 2983,44 l 2978,45 l 2978,-135 l 2978,-135 l 2968,-135 l 2968,45 l 2982,54 l 2985,54 l 2989,53 l 2992,52 l 2995,52 l 2998,51 l 3001,50 l 3005,49 l 3008,48 l 3011,47 l 3014,45 l 3017,44 l 3020,43 l 3023,42 l 3026,40 l 3028,39 l 3031,38 l 3034,36 l 3037,34 l 3040,32 l 3043,30 l 3045,29 l 3048,27 l 3050,25 l 3053,23 l 3055,21 l 3057,19 l 3059,17 l 3062,15 l 3064,13 l 3066,11 l 3066,11 l 3069,-180 l 3050,-184 l 3048,-182 l 3046,-181 l 3044,-179 l 3042,-177 l 3040,-175 l 3037,-173 l 3035,-171 l 3032,-169 l 3030,-167 l 3027,-165 l 3024,-164 l 3022,-162 l 3019,-160 l 3017,-159 l 3014,-158 l 3012,-157 l 3009,-155 l 3006,-154 l 3003,-153 l 3000,-151 l 2997,-150 l 2994,-149 l 2991,-149 l 2988,-148 l 2986,-147 l 2983,-146 l 2980,-146 l 2977,-145 l 2976,-145xe" onmouseover="UpdateTooltip(this,0,192)" onclick="OnShapeClick(0,192)" onfocus="UpdateTooltip(this,0,192);" onkeyup="OnShapeKey(0,192)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 3059,3 l 3059,3 l 3059,3 l 2927,-68 l 2845,-24 l 2845,-24 l 2845,156 l 2845,156 l 2849,158 l 2978,227 l 2978,227 l 2983,227 l 2987,226 l 2992,225 l 2997,223 l 3002,222 l 3006,220 l 3011,218 l 3016,217 l 3020,214 l 3024,212 l 3029,210 l 3033,207 l 3037,205 l 3041,202 l 3045,199 l 3049,196 l 3052,193 l 3056,190 l 3059,186 l 3059,186 l 3059,3 l 3059,3 l 3059,3xe" onmouseover="UpdateTooltip(this,0,207)" onclick="OnShapeClick(0,207)" onfocus="UpdateTooltip(this,0,207);" onkeyup="OnShapeKey(0,207)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 2978,47 l 2845,-24 l 2927,-68 l 3059,3 l 3056,7 l 3052,10 l 3049,13 l 3045,16 l 3041,20 l 3037,22 l 3033,25 l 3029,28 l 3025,30 l 3020,33 l 3016,35 l 3011,37 l 3007,39 l 3002,41 l 2997,43 l 2992,44 l 2988,45 l 2983,46 l 2978,47 l 2978,47 l 2980,57 l 2983,57 l 2986,56 l 2989,55 l 2992,54 l 2996,54 l 2999,53 l 3002,51 l 3005,50 l 3008,49 l 3011,48 l 3015,47 l 3018,45 l 3021,44 l 3024,42 l 3026,41 l 3029,40 l 3031,38 l 3035,36 l 3038,34 l 3041,32 l 3043,30 l 3046,29 l 3048,27 l 3051,25 l 3053,23 l 3055,21 l 3057,19 l 3060,17 l 3062,15 l 3064,13 l 3066,10 l 3067,10 l 2932,-77 l 2840,-33 l 2973,56xe" onmouseover="UpdateTooltip(this,0,207)" onclick="OnShapeClick(0,207)" onfocus="UpdateTooltip(this,0,207);" onkeyup="OnShapeKey(0,207)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m 2978,47 l 2983,46 l 2988,45 l 2992,44 l 2997,43 l 3002,41 l 3007,39 l 3011,37 l 3016,35 l 3020,33 l 3025,30 l 3029,28 l 3033,25 l 3037,22 l 3041,19 l 3045,16 l 3049,13 l 3052,10 l 3056,6 l 3059,3 l 3059,186 l 3056,190 l 3052,193 l 3049,196 l 3045,199 l 3041,202 l 3037,205 l 3033,207 l 3029,210 l 3024,212 l 3020,214 l 3016,217 l 3011,218 l 3006,220 l 3002,222 l 2997,223 l 2992,225 l 2987,226 l 2983,227 l 2978,227 l 2978,47 l 2978,47 l 2968,47 l 2968,227 l 2982,237 l 2985,236 l 2989,236 l 2992,235 l 2995,234 l 2998,233 l 3001,232 l 3005,231 l 3008,230 l 3011,229 l 3014,228 l 3017,227 l 3020,225 l 3023,224 l 3026,223 l 3028,222 l 3031,220 l 3034,218 l 3037,216 l 3040,215 l 3043,213 l 3045,211 l 3048,209 l 3050,208 l 3053,206 l 3055,204 l 3057,202 l 3059,200 l 3062,198 l 3064,196 l 3066,194 l 3066,193 l 3069,3 l 3050,-2 l 3048,0 l 3046,2 l 3044,4 l 3042,6 l 3040,8 l 3037,10 l 3035,12 l 3032,14 l 3030,15 l 3027,17 l 3024,19 l 3022,21 l 3019,22 l 3017,23 l 3014,25 l 3012,26 l 3009,27 l 3006,29 l 3003,30 l 3000,31 l 2997,32 l 2994,33 l 2991,34 l 2988,35 l 2986,36 l 2983,36 l 2980,37 l 2977,37 l 2976,38xe" onmouseover="UpdateTooltip(this,0,207)" onclick="OnShapeClick(0,207)" onfocus="UpdateTooltip(this,0,207);" onkeyup="OnShapeKey(0,207)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_Entity:192.168.0.43(Unix)+color" tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:4191;height:2000;position:absolute" path=" m -745,-563 l 21,-563 l 21,-697 l -745,-697 l -745,-563xe" onmouseover="UpdateTooltip(this,0,245)" onclick="OnShapeClick(0,245)" onfocus="UpdateTooltip(this,0,245);" onkeyup="OnShapeKey(0,245)"/> 

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
