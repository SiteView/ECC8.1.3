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


		viewMgr.visBBoxLeft = 10.657275;
		viewMgr.visBBoxRight = 208.907540;
		viewMgr.visBBoxBottom = -0.075498;
		viewMgr.visBBoxTop = 165.917815;

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


<v:group  id="ConvertedImage" style="width:4.777307in;height:4.000000in;position:absolute;visibility=hidden;" coordSize="2389,2000" coordOrigin="-1000,-1000" >

<v:shapetype 
  id="VISSHAPE"
  target="_parent"
  coordsize="2389,2000"
  coordorigin="-1000,-1000"
  stroked="f"
  strokecolor="red"
  filled="t"
>
<v:fill opacity="0.0"/>
</v:shapetype>


<v:shape style="top:-1000.0;left:-1000.0;width:2388.7;height:2000.0;position:absolute" coordSize="2389,2000" coordOrigin="-1000,-1000" >
<v:imagedata src="vml_1.emz"/>
</v:shape>

<v:shape type="#VISSHAPE"   tabindex="1" title="机柜１" origTitle="机柜１"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -511,-923 l -591,-923 l -591,-869 l -511,-869 l -471,-869 l -471,-923 l -511,-923x m -751,-988 l -801,-988 l -801,-934 l -751,-934 l -722,-934 l -722,-988 l -751,-988x m -654,-33 l -870,-33 l -870,-915 l -654,-915 l -654,-33x m -636,-915 l -636,-934 l -888,-934 l -888,-915 l -888,-33 l -888,3 l -636,3 l -636,-33 l -636,-915xe" onmouseover="UpdateTooltip(this,0,1)" onclick="OnShapeClick(0,1)" onfocus="UpdateTooltip(this,0,1);" onkeyup="OnShapeKey(0,1)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_Monitor:Memory+color" tabindex="1" title="UNIX小机 93" origTitle="UNIX小机 93"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -580,-452 l -529,-452 l -449,-452 l -449,-506 l -529,-506 l -625,-506 l -625,-457 l -625,-452 l -625,-403 l -580,-403 l -580,-452x m -950,-483 l -980,-483 l -980,-429 l -950,-429 l -922,-429 l -922,-483 l -950,-483x m -653,-495 l -870,-495 l -881,-495 l -881,-474 l -870,-474 l -870,-432 l -881,-432 l -881,-411 l -870,-411 l -653,-411 l -642,-411 l -642,-432 l -653,-432 l -653,-474 l -642,-474 l -642,-495 l -653,-495xe" onmouseover="UpdateTooltip(this,0,126)" onclick="OnShapeClick(0,126)" onfocus="UpdateTooltip(this,0,126);" onkeyup="OnShapeKey(0,126)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_Monitor:Memory+color" tabindex="1" title="UNIX小机 93" origTitle="UNIX小机 93"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -858,-480 l -858,-495 l -858,-495 l -868,-495 l -868,-480xe" onmouseover="UpdateTooltip(this,0,126)" onclick="OnShapeClick(0,126)" onfocus="UpdateTooltip(this,0,126);" onkeyup="OnShapeKey(0,126)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_Monitor:Memory+color" tabindex="1" title="UNIX小机 93" origTitle="UNIX小机 93"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -665,-480 l -665,-495 l -665,-495 l -675,-495 l -675,-480xe" onmouseover="UpdateTooltip(this,0,126)" onclick="OnShapeClick(0,126)" onfocus="UpdateTooltip(this,0,126);" onkeyup="OnShapeKey(0,126)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -950,-409 l -980,-409 l -980,-355 l -950,-355 l -922,-355 l -922,-409 l -950,-409x m -653,-390 l -870,-390 l -881,-390 l -881,-369 l -870,-369 l -653,-369 l -642,-369 l -642,-390 l -653,-390xe" onmouseover="UpdateTooltip(this,0,150)" onclick="OnShapeClick(0,150)" onfocus="UpdateTooltip(this,0,150);" onkeyup="OnShapeKey(0,150)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -721,-616 l -721,-616 l -720,-616 l -719,-616 l -719,-616 l -718,-616 l -717,-616 l -717,-617 l -716,-617 l -715,-617 l -715,-617 l -714,-617 l -713,-618 l -713,-618 l -712,-618 l -711,-618 l -711,-618 l -710,-618 l -710,-619 l -709,-619 l -708,-619 l -708,-619 l -707,-620 l -706,-620 l -706,-620 l -705,-620 l -704,-620 l -704,-621 l -703,-621 l -702,-621 l -702,-621 l -701,-622 l -700,-622 l -700,-622 l -699,-622 l -699,-622 l -698,-623 l -697,-623 l -697,-623 l -696,-623 l -695,-624 l -695,-624 l -694,-624 l -694,-625 l -693,-625 l -692,-625 l -692,-625 l -691,-626 l -690,-626 l -690,-626 l -651,-626 l -651,-812 l -864,-812 l -864,-626 l -826,-626 l -825,-626 l -825,-626 l -824,-625 l -823,-625 l -823,-625 l -822,-625 l -821,-624 l -821,-624 l -820,-624 l -819,-623 l -819,-623 l -818,-623 l -818,-623 l -817,-622 l -816,-622 l -816,-622 l -815,-622 l -814,-622 l -814,-621 l -813,-621 l -812,-621 l -812,-621 l -811,-620 l -811,-620 l -810,-620 l -809,-620 l -809,-620 l -808,-619 l -807,-619 l -807,-619 l -806,-619 l -805,-618 l -805,-618 l -804,-618 l -803,-618 l -803,-618 l -802,-618 l -801,-617 l -801,-617 l -800,-617 l -799,-617 l -799,-617 l -798,-616 l -798,-616 l -797,-616 l -796,-616 l -796,-616 l -795,-616 l -794,-616 l -823,-606 l -826,-600 l -690,-600 l -693,-606 l -721,-616xe" onmouseover="UpdateTooltip(this,0,162)" onclick="OnShapeClick(0,162)" onfocus="UpdateTooltip(this,0,162);" onkeyup="OnShapeKey(0,162)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -950,-619 l -980,-619 l -980,-565 l -950,-565 l -922,-565 l -922,-619 l -950,-619x m -653,-600 l -870,-600 l -881,-600 l -881,-579 l -870,-579 l -653,-579 l -642,-579 l -642,-600 l -653,-600xe" onmouseover="UpdateTooltip(this,0,168)" onclick="OnShapeClick(0,168)" onfocus="UpdateTooltip(this,0,168);" onkeyup="OnShapeKey(0,168)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="机柜2" origTitle="机柜2"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 340,-931 l 260,-931 l 260,-877 l 340,-877 l 360,-877 l 360,-931 l 340,-931x m 100,-996 l 50,-996 l 50,-942 l 100,-942 l 129,-942 l 129,-996 l 100,-996x m 198,-41 l -19,-41 l -19,-923 l 198,-923 l 198,-41x m 216,-923 l 216,-942 l -37,-942 l -37,-923 l -37,-41 l -37,-6 l 216,-6 l 216,-41 l 216,-923xe" onmouseover="UpdateTooltip(this,0,179)" onclick="OnShapeClick(0,179)" onfocus="UpdateTooltip(this,0,179);" onkeyup="OnShapeKey(0,179)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -99,-491 l -129,-491 l -129,-437 l -99,-437 l -70,-437 l -70,-491 l -99,-491x m 198,-472 l -19,-472 l -30,-472 l -30,-451 l -19,-451 l 198,-451 l 209,-451 l 209,-472 l 198,-472xe" onmouseover="UpdateTooltip(this,0,328)" onclick="OnShapeClick(0,328)" onfocus="UpdateTooltip(this,0,328);" onkeyup="OnShapeKey(0,328)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 130,-624 l 130,-624 l 131,-624 l 132,-625 l 132,-625 l 133,-625 l 134,-625 l 134,-625 l 135,-625 l 136,-626 l 136,-626 l 137,-626 l 138,-626 l 138,-626 l 139,-626 l 140,-627 l 140,-627 l 141,-627 l 142,-627 l 142,-627 l 143,-628 l 144,-628 l 144,-628 l 145,-628 l 146,-628 l 146,-629 l 147,-629 l 147,-629 l 148,-629 l 149,-630 l 149,-630 l 150,-630 l 151,-630 l 151,-631 l 152,-631 l 153,-631 l 153,-631 l 154,-632 l 154,-632 l 155,-632 l 156,-632 l 156,-633 l 157,-633 l 158,-633 l 158,-633 l 159,-634 l 159,-634 l 160,-634 l 161,-634 l 161,-635 l 200,-635 l 200,-821 l -13,-821 l -13,-635 l 25,-635 l 26,-634 l 27,-634 l 27,-634 l 28,-634 l 29,-633 l 29,-633 l 30,-633 l 30,-633 l 31,-632 l 32,-632 l 32,-632 l 33,-632 l 34,-631 l 34,-631 l 35,-631 l 35,-631 l 36,-630 l 37,-630 l 37,-630 l 38,-630 l 39,-629 l 39,-629 l 40,-629 l 41,-629 l 41,-628 l 42,-628 l 43,-628 l 43,-628 l 44,-628 l 44,-627 l 45,-627 l 46,-627 l 46,-627 l 47,-627 l 48,-626 l 48,-626 l 49,-626 l 50,-626 l 50,-626 l 51,-626 l 52,-625 l 52,-625 l 53,-625 l 54,-625 l 54,-625 l 55,-625 l 56,-624 l 56,-624 l 57,-624 l 28,-614 l 25,-608 l 161,-608 l 158,-614 l 130,-624xe" onmouseover="UpdateTooltip(this,0,340)" onclick="OnShapeClick(0,340)" onfocus="UpdateTooltip(this,0,340);" onkeyup="OnShapeKey(0,340)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -99,-628 l -129,-628 l -129,-574 l -99,-574 l -70,-574 l -70,-628 l -99,-628x m 198,-608 l -19,-608 l -30,-608 l -30,-587 l -19,-587 l 198,-587 l 209,-587 l 209,-608 l 198,-608xe" onmouseover="UpdateTooltip(this,0,346)" onclick="OnShapeClick(0,346)" onfocus="UpdateTooltip(this,0,346);" onkeyup="OnShapeKey(0,346)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="IBM DS3400存储" origTitle="IBM DS3400存储"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 453,-124 l 364,-124 l 226,-124 l 226,-70 l 364,-70 l 453,-70 l 533,-70 l 533,-124 l 453,-124x m -99,-125 l -129,-125 l -129,-71 l -99,-71 l -70,-71 l -70,-125 l -99,-125x m -30,-63 l 209,-63 l 209,-126 l -30,-126 l -30,-63xe" onmouseover="UpdateTooltip(this,0,280)" onclick="OnShapeClick(0,280)" onfocus="UpdateTooltip(this,0,280);" onkeyup="OnShapeKey(0,280)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_IP:127.0.0.1+color" tabindex="1" title="龙山税友243" origTitle="龙山税友243"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 387,-368 l 226,-368 l 226,-314 l 387,-314 l 453,-314 l 453,-368 l 387,-368x m -99,-369 l -129,-369 l -129,-315 l -99,-315 l -70,-315 l -70,-369 l -99,-369x m 198,-360 l -19,-360 l -30,-360 l -30,-339 l -30,-318 l -19,-318 l 198,-318 l 209,-318 l 209,-339 l 209,-360 l 198,-360xe" onmouseover="UpdateTooltip(this,0,184)" onclick="OnShapeClick(0,184)" onfocus="UpdateTooltip(this,0,184);" onkeyup="OnShapeKey(0,184)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_IP:127.0.0.1+color" tabindex="1" title="龙山税友243" origTitle="龙山税友243"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -7,-346 l -7,-360 l -7,-360 l -17,-360 l -17,-346xe" onmouseover="UpdateTooltip(this,0,184)" onclick="OnShapeClick(0,184)" onfocus="UpdateTooltip(this,0,184);" onkeyup="OnShapeKey(0,184)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_IP:127.0.0.1+color" tabindex="1" title="龙山税友243" origTitle="龙山税友243"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 186,-346 l 186,-360 l 186,-360 l 176,-360 l 176,-346xe" onmouseover="UpdateTooltip(this,0,184)" onclick="OnShapeClick(0,184)" onfocus="UpdateTooltip(this,0,184);" onkeyup="OnShapeKey(0,184)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="机柜３" origTitle="机柜３"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 1034,-931 l 954,-931 l 954,-877 l 1034,-877 l 1074,-877 l 1074,-931 l 1034,-931x m 794,-996 l 743,-996 l 743,-942 l 794,-942 l 822,-942 l 822,-996 l 794,-996x m 891,-41 l 675,-41 l 675,-923 l 891,-923 l 891,-41x m 909,-923 l 909,-942 l 657,-942 l 657,-923 l 657,-41 l 657,-6 l 909,-6 l 909,-41 l 909,-923xe" onmouseover="UpdateTooltip(this,0,256)" onclick="OnShapeClick(0,256)" onfocus="UpdateTooltip(this,0,256);" onkeyup="OnShapeKey(0,256)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_Monitor:Cpu+color" tabindex="1" title="华为6503交换机1.1" origTitle="华为6503交换机1.1"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 1243,-272 l 1231,-272 l 1209,-272 l 1089,-272 l 1000,-272 l 920,-272 l 920,-218 l 1000,-218 l 1089,-218 l 1209,-218 l 1231,-218 l 1243,-218 l 1265,-218 l 1265,-272 l 1243,-272x m 594,-273 l 564,-273 l 564,-219 l 594,-219 l 623,-219 l 623,-273 l 594,-273x m 663,-201 l 903,-201 l 903,-285 l 663,-285 l 663,-201xe" onmouseover="UpdateTooltip(this,0,277)" onclick="OnShapeClick(0,277)" onfocus="UpdateTooltip(this,0,277);" onkeyup="OnShapeKey(0,277)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="PDU" origTitle="PDU"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -625,-577 l -625,-523 l -540,-523 l -540,-577 l -625,-577x m -950,-577 l -980,-577 l -980,-523 l -950,-523 l -922,-523 l -922,-577 l -950,-577x m -881,-537 l -642,-537 l -642,-558 l -881,-558 l -881,-537xe" onmouseover="UpdateTooltip(this,0,397)" onclick="OnShapeClick(0,397)" onfocus="UpdateTooltip(this,0,397);" onkeyup="OnShapeKey(0,397)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="PDU" origTitle="PDU"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 226,-586 l 226,-532 l 311,-532 l 311,-586 l 226,-586x m -99,-586 l -129,-586 l -129,-532 l -99,-532 l -70,-532 l -70,-586 l -99,-586x m -30,-545 l 209,-545 l 209,-566 l -30,-566 l -30,-545xe" onmouseover="UpdateTooltip(this,0,409)" onclick="OnShapeClick(0,409)" onfocus="UpdateTooltip(this,0,409);" onkeyup="OnShapeKey(0,409)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="H3C1526接入交换机" origTitle="H3C1526接入交换机"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 1089,-732 l 1000,-732 l 971,-732 l 949,-732 l 920,-732 l 920,-678 l 949,-678 l 971,-678 l 1000,-678 l 1089,-678 l 1289,-678 l 1289,-732 l 1089,-732x m 594,-733 l 564,-733 l 564,-679 l 594,-679 l 623,-679 l 623,-733 l 594,-733x m 663,-692 l 903,-692 l 903,-713 l 663,-713 l 663,-692xe" onmouseover="UpdateTooltip(this,0,421)" onclick="OnShapeClick(0,421)" onfocus="UpdateTooltip(this,0,421);" onkeyup="OnShapeKey(0,421)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="H3C 1526接入交换机" origTitle="H3C 1526接入交换机"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 1100,-690 l 1011,-690 l 971,-690 l 949,-690 l 920,-690 l 920,-636 l 949,-636 l 971,-636 l 1011,-636 l 1100,-636 l 1300,-636 l 1300,-690 l 1100,-690x m 594,-691 l 564,-691 l 564,-637 l 594,-637 l 623,-637 l 623,-691 l 594,-691x m 663,-650 l 903,-650 l 903,-671 l 663,-671 l 663,-650xe" onmouseover="UpdateTooltip(this,0,437)" onclick="OnShapeClick(0,437)" onfocus="UpdateTooltip(this,0,437);" onkeyup="OnShapeKey(0,437)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="PDU" origTitle="PDU"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 920,-838 l 920,-784 l 1004,-784 l 1004,-838 l 920,-838x m 594,-838 l 564,-838 l 564,-784 l 594,-784 l 623,-784 l 623,-838 l 594,-838x m 663,-797 l 903,-797 l 903,-818 l 663,-818 l 663,-797xe" onmouseover="UpdateTooltip(this,0,469)" onclick="OnShapeClick(0,469)" onfocus="UpdateTooltip(this,0,469);" onkeyup="OnShapeKey(0,469)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="PDU" origTitle="PDU"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 920,-82 l 920,-28 l 1004,-28 l 1004,-82 l 920,-82x m 594,-82 l 564,-82 l 564,-28 l 594,-28 l 623,-28 l 623,-82 l 594,-82x m 663,-41 l 903,-41 l 903,-62 l 663,-62 l 663,-41xe" onmouseover="UpdateTooltip(this,0,481)" onclick="OnShapeClick(0,481)" onfocus="UpdateTooltip(this,0,481);" onkeyup="OnShapeKey(0,481)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="机柜４" origTitle="机柜４"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -526,70 l -607,70 l -607,124 l -526,124 l -486,124 l -486,70 l -526,70x m -767,6 l -817,6 l -817,60 l -767,60 l -738,60 l -738,6 l -767,6x m -669,961 l -886,961 l -886,79 l -669,79 l -669,961x m -651,79 l -651,60 l -904,60 l -904,79 l -904,961 l -904,996 l -651,996 l -651,961 l -651,79xe" onmouseover="UpdateTooltip(this,0,493)" onclick="OnShapeClick(0,493)" onfocus="UpdateTooltip(this,0,493);" onkeyup="OnShapeKey(0,493)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -736,330 l -735,330 l -734,330 l -734,330 l -733,330 l -732,330 l -732,330 l -731,329 l -730,329 l -730,329 l -729,329 l -728,329 l -728,328 l -727,328 l -726,328 l -726,328 l -725,328 l -724,328 l -724,327 l -723,327 l -722,327 l -722,327 l -721,326 l -720,326 l -720,326 l -719,326 l -719,326 l -718,325 l -717,325 l -717,325 l -716,325 l -715,324 l -715,324 l -714,324 l -713,324 l -713,324 l -712,323 l -712,323 l -711,323 l -710,323 l -710,322 l -709,322 l -708,322 l -708,321 l -707,321 l -706,321 l -706,321 l -705,320 l -705,320 l -704,320 l -666,320 l -666,134 l -878,134 l -878,320 l -840,320 l -839,320 l -839,320 l -838,321 l -837,321 l -837,321 l -836,321 l -836,322 l -835,322 l -834,322 l -834,323 l -833,323 l -832,323 l -832,323 l -831,324 l -831,324 l -830,324 l -829,324 l -829,324 l -828,325 l -827,325 l -827,325 l -826,325 l -825,326 l -825,326 l -824,326 l -823,326 l -823,326 l -822,327 l -822,327 l -821,327 l -820,327 l -820,328 l -819,328 l -818,328 l -818,328 l -817,328 l -816,328 l -816,329 l -815,329 l -814,329 l -814,329 l -813,329 l -812,330 l -812,330 l -811,330 l -810,330 l -810,330 l -809,330 l -808,330 l -837,340 l -840,346 l -704,346 l -707,340 l -736,330xe" onmouseover="UpdateTooltip(this,0,570)" onclick="OnShapeClick(0,570)" onfocus="UpdateTooltip(this,0,570);" onkeyup="OnShapeKey(0,570)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="" origTitle=""  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -954,327 l -984,327 l -984,381 l -954,381 l -925,381 l -925,327 l -954,327x m -657,346 l -874,346 l -885,346 l -885,367 l -874,367 l -657,367 l -646,367 l -646,346 l -657,346xe" onmouseover="UpdateTooltip(this,0,576)" onclick="OnShapeClick(0,576)" onfocus="UpdateTooltip(this,0,576);" onkeyup="OnShapeKey(0,576)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="PDU" origTitle="PDU"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -641,899 l -641,953 l -556,953 l -556,899 l -641,899x m -966,899 l -996,899 l -996,953 l -966,953 l -937,953 l -937,899 l -966,899x m -897,940 l -658,940 l -658,919 l -897,919 l -897,940xe" onmouseover="UpdateTooltip(this,0,587)" onclick="OnShapeClick(0,587)" onfocus="UpdateTooltip(this,0,587);" onkeyup="OnShapeKey(0,587)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="机柜５" origTitle="机柜５"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 199,61 l 119,61 l 119,115 l 199,115 l 239,115 l 239,61 l 199,61x m -42,-4 l -92,-4 l -92,50 l -42,50 l -13,50 l -13,-4 l -42,-4x m 56,951 l -160,951 l -160,69 l 56,69 l 56,951x m 74,69 l 74,50 l -179,50 l -179,69 l -179,951 l -179,987 l 74,987 l 74,951 l 74,69xe" onmouseover="UpdateTooltip(this,0,599)" onclick="OnShapeClick(0,599)" onfocus="UpdateTooltip(this,0,599);" onkeyup="OnShapeKey(0,599)"/> 

<v:shape type="#VISSHAPE"  href="#" fillcolor="SV_IP:192.168.1.4+color" tabindex="1" title="华为2403服务器交换机1.4" origTitle="华为2403服务器交换机1.4"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 527,303 l 516,303 l 494,303 l 254,303 l 165,303 l 85,303 l 85,357 l 165,357 l 254,357 l 494,357 l 516,357 l 527,357 l 550,357 l 550,303 l 527,303x m -241,302 l -271,302 l -271,356 l -241,356 l -212,356 l -212,302 l -241,302x m -172,342 l 67,342 l 67,321 l -172,321 l -172,342xe" onmouseover="UpdateTooltip(this,0,604)" onclick="OnShapeClick(0,604)" onfocus="UpdateTooltip(this,0,604);" onkeyup="OnShapeKey(0,604)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="华为30-20路由器200" origTitle="华为30-20路由器200"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 393,567 l 273,567 l 228,567 l 215,567 l 170,567 l 90,567 l 90,621 l 170,621 l 215,621 l 228,621 l 273,621 l 393,621 l 460,621 l 460,567 l 393,567x m -235,566 l -265,566 l -265,620 l -235,620 l -207,620 l -207,566 l -235,566x m -167,615 l -137,615 l -137,617 l 5,617 l 5,615 l 73,615 l 73,578 l 5,578 l 5,575 l -137,575 l -137,578 l -167,578 l -167,615xe" onmouseover="UpdateTooltip(this,0,620)" onclick="OnShapeClick(0,620)" onfocus="UpdateTooltip(this,0,620);" onkeyup="OnShapeKey(0,620)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="华为30-20路由器200" origTitle="华为30-20路由器200"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -137,615 l -137,578 l -137,578 l -147,578 l -147,615xe" onmouseover="UpdateTooltip(this,0,620)" onclick="OnShapeClick(0,620)" onfocus="UpdateTooltip(this,0,620);" onkeyup="OnShapeKey(0,620)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="华为30-20路由器200" origTitle="华为30-20路由器200"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 5,615 l 5,578 l 5,578 l -5,578 l -5,615xe" onmouseover="UpdateTooltip(this,0,620)" onclick="OnShapeClick(0,620)" onfocus="UpdateTooltip(this,0,620);" onkeyup="OnShapeKey(0,620)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="华为30-20路由器200" origTitle="华为30-20路由器200"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,615 l -155,578 l -155,578 l -165,578 l -165,615xe" onmouseover="UpdateTooltip(this,0,620)" onclick="OnShapeClick(0,620)" onfocus="UpdateTooltip(this,0,620);" onkeyup="OnShapeKey(0,620)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="华为30-20路由器200" origTitle="华为30-20路由器200"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 62,615 l 62,578 l 62,578 l 52,578 l 52,615xe" onmouseover="UpdateTooltip(this,0,620)" onclick="OnShapeClick(0,620)" onfocus="UpdateTooltip(this,0,620);" onkeyup="OnShapeKey(0,620)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="华为30-20路由器200" origTitle="华为30-20路由器200"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -66,617 l -66,575 l -66,575 l -76,575 l -76,617xe" onmouseover="UpdateTooltip(this,0,620)" onclick="OnShapeClick(0,620)" onfocus="UpdateTooltip(this,0,620);" onkeyup="OnShapeKey(0,620)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 90,504 l 90,558 l 330,558 l 330,504 l 90,504x m -235,503 l -265,503 l -265,557 l -235,557 l -207,557 l -207,503 l -235,503x m 62,554 l 73,554 l 73,533 l 62,533 l 62,554x m -167,554 l -155,554 l -155,533 l -167,533 l -167,554x m -141,543 l -128,543 l -128,546 l -121,546 l -121,543 l -107,543 l -107,540 l -141,540 l -141,543x m -117,549 l -114,549 l -114,546 l -117,546 l -117,549x m -135,549 l -132,549 l -132,546 l -135,546 l -135,549xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,551 l 62,551 l 62,551 l 62,541 l -155,541xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,549 l 62,549 l 62,549 l 62,539 l -155,539xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,546 l 62,546 l 62,546 l 62,536 l -155,536xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,543 l 62,543 l 62,543 l 62,533 l -155,533xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,540 l 62,540 l 62,540 l 62,530 l -155,530xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,537 l 62,537 l 62,537 l 62,527 l -155,527xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="奥科中继网关" origTitle="奥科中继网关"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m -155,534 l 62,534 l 62,534 l 62,524 l -155,524xe" onmouseover="UpdateTooltip(this,0,632)" onclick="OnShapeClick(0,632)" onfocus="UpdateTooltip(this,0,632);" onkeyup="OnShapeKey(0,632)"/> 

<v:shape type="#VISSHAPE"   tabindex="1" title="铁通光端机" origTitle="铁通光端机"  style="top:-1000;left:-1000;width:2389;height:2000;position:absolute" path=" m 85,397 l 85,451 l 285,451 l 285,397 l 85,397x m -241,396 l -271,396 l -271,450 l -241,450 l -212,450 l -212,396 l -241,396x m -172,447 l 67,447 l 67,405 l -172,405 l -172,447xe" onmouseover="UpdateTooltip(this,0,646)" onclick="OnShapeClick(0,646)" onfocus="UpdateTooltip(this,0,646);" onkeyup="OnShapeKey(0,646)"/> 

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
