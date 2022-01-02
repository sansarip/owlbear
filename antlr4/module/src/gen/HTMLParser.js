// Generated from /Users/sansarip/Dev/owlbear/antlr4/HTMLParser.g4 by ANTLR 4.8
// jshint ignore: start
var antlr4 = require('antlr4/index');
var grammarFileName = "HTMLParser.g4";


var serializedATN = ["\u0003\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964",
    "\u0003\u0019\u008f\u0004\u0002\t\u0002\u0004\u0003\t\u0003\u0004\u0004",
    "\t\u0004\u0004\u0005\t\u0005\u0004\u0006\t\u0006\u0004\u0007\t\u0007",
    "\u0004\b\t\b\u0004\t\t\t\u0004\n\t\n\u0004\u000b\t\u000b\u0004\f\t\f",
    "\u0004\r\t\r\u0003\u0002\u0007\u0002\u001c\n\u0002\f\u0002\u000e\u0002",
    "\u001f\u000b\u0002\u0003\u0003\u0007\u0003\"\n\u0003\f\u0003\u000e\u0003",
    "%\u000b\u0003\u0003\u0003\u0005\u0003(\n\u0003\u0003\u0003\u0007\u0003",
    "+\n\u0003\f\u0003\u000e\u0003.\u000b\u0003\u0003\u0003\u0005\u00031",
    "\n\u0003\u0003\u0003\u0007\u00034\n\u0003\f\u0003\u000e\u00037\u000b",
    "\u0003\u0003\u0003\u0007\u0003:\n\u0003\f\u0003\u000e\u0003=\u000b\u0003",
    "\u0003\u0004\u0003\u0004\u0003\u0005\u0007\u0005B\n\u0005\f\u0005\u000e",
    "\u0005E\u000b\u0005\u0003\u0005\u0003\u0005\u0007\u0005I\n\u0005\f\u0005",
    "\u000e\u0005L\u000b\u0005\u0003\u0006\u0003\u0006\u0003\u0006\u0007",
    "\u0006Q\n\u0006\f\u0006\u000e\u0006T\u000b\u0006\u0003\u0006\u0003\u0006",
    "\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0005\u0006",
    "]\n\u0006\u0003\u0006\u0005\u0006`\n\u0006\u0003\u0006\u0003\u0006\u0003",
    "\u0006\u0005\u0006e\n\u0006\u0003\u0007\u0005\u0007h\n\u0007\u0003\u0007",
    "\u0003\u0007\u0003\u0007\u0003\u0007\u0005\u0007n\n\u0007\u0003\u0007",
    "\u0005\u0007q\n\u0007\u0007\u0007s\n\u0007\f\u0007\u000e\u0007v\u000b",
    "\u0007\u0003\b\u0003\b\u0003\b\u0005\b{\n\b\u0003\t\u0003\t\u0003\n",
    "\u0003\n\u0003\n\u0003\n\u0003\n\u0003\n\u0005\n\u0085\n\n\u0003\u000b",
    "\u0003\u000b\u0003\f\u0003\f\u0003\f\u0003\r\u0003\r\u0003\r\u0003\r",
    "\u0002\u0002\u000e\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016",
    "\u0018\u0002\u0007\u0003\u0002\b\t\u0004\u0002\t\t\r\r\u0004\u0002\u0003",
    "\u0003\u0005\u0005\u0003\u0002\u0014\u0015\u0003\u0002\u0016\u0017\u0002",
    "\u009d\u0002\u001d\u0003\u0002\u0002\u0002\u0004#\u0003\u0002\u0002",
    "\u0002\u0006>\u0003\u0002\u0002\u0002\bC\u0003\u0002\u0002\u0002\nd",
    "\u0003\u0002\u0002\u0002\fg\u0003\u0002\u0002\u0002\u000ew\u0003\u0002",
    "\u0002\u0002\u0010|\u0003\u0002\u0002\u0002\u0012\u0084\u0003\u0002",
    "\u0002\u0002\u0014\u0086\u0003\u0002\u0002\u0002\u0016\u0088\u0003\u0002",
    "\u0002\u0002\u0018\u008b\u0003\u0002\u0002\u0002\u001a\u001c\u0005\b",
    "\u0005\u0002\u001b\u001a\u0003\u0002\u0002\u0002\u001c\u001f\u0003\u0002",
    "\u0002\u0002\u001d\u001b\u0003\u0002\u0002\u0002\u001d\u001e\u0003\u0002",
    "\u0002\u0002\u001e\u0003\u0003\u0002\u0002\u0002\u001f\u001d\u0003\u0002",
    "\u0002\u0002 \"\u0005\u0006\u0004\u0002! \u0003\u0002\u0002\u0002\"",
    "%\u0003\u0002\u0002\u0002#!\u0003\u0002\u0002\u0002#$\u0003\u0002\u0002",
    "\u0002$\'\u0003\u0002\u0002\u0002%#\u0003\u0002\u0002\u0002&(\u0007",
    "\u0006\u0002\u0002\'&\u0003\u0002\u0002\u0002\'(\u0003\u0002\u0002\u0002",
    "(,\u0003\u0002\u0002\u0002)+\u0005\u0006\u0004\u0002*)\u0003\u0002\u0002",
    "\u0002+.\u0003\u0002\u0002\u0002,*\u0003\u0002\u0002\u0002,-\u0003\u0002",
    "\u0002\u0002-0\u0003\u0002\u0002\u0002.,\u0003\u0002\u0002\u0002/1\u0007",
    "\u0007\u0002\u00020/\u0003\u0002\u0002\u000201\u0003\u0002\u0002\u0002",
    "15\u0003\u0002\u0002\u000224\u0005\u0006\u0004\u000232\u0003\u0002\u0002",
    "\u000247\u0003\u0002\u0002\u000253\u0003\u0002\u0002\u000256\u0003\u0002",
    "\u0002\u00026;\u0003\u0002\u0002\u000275\u0003\u0002\u0002\u00028:\u0005",
    "\b\u0005\u000298\u0003\u0002\u0002\u0002:=\u0003\u0002\u0002\u0002;",
    "9\u0003\u0002\u0002\u0002;<\u0003\u0002\u0002\u0002<\u0005\u0003\u0002",
    "\u0002\u0002=;\u0003\u0002\u0002\u0002>?\t\u0002\u0002\u0002?\u0007",
    "\u0003\u0002\u0002\u0002@B\u0005\u0012\n\u0002A@\u0003\u0002\u0002\u0002",
    "BE\u0003\u0002\u0002\u0002CA\u0003\u0002\u0002\u0002CD\u0003\u0002\u0002",
    "\u0002DF\u0003\u0002\u0002\u0002EC\u0003\u0002\u0002\u0002FJ\u0005\n",
    "\u0006\u0002GI\u0005\u0012\n\u0002HG\u0003\u0002\u0002\u0002IL\u0003",
    "\u0002\u0002\u0002JH\u0003\u0002\u0002\u0002JK\u0003\u0002\u0002\u0002",
    "K\t\u0003\u0002\u0002\u0002LJ\u0003\u0002\u0002\u0002MN\u0007\f\u0002",
    "\u0002NR\u0007\u0012\u0002\u0002OQ\u0005\u000e\b\u0002PO\u0003\u0002",
    "\u0002\u0002QT\u0003\u0002\u0002\u0002RP\u0003\u0002\u0002\u0002RS\u0003",
    "\u0002\u0002\u0002S_\u0003\u0002\u0002\u0002TR\u0003\u0002\u0002\u0002",
    "U\\\u0007\u000e\u0002\u0002VW\u0005\f\u0007\u0002WX\u0007\f\u0002\u0002",
    "XY\u0007\u0010\u0002\u0002YZ\u0007\u0012\u0002\u0002Z[\u0007\u000e\u0002",
    "\u0002[]\u0003\u0002\u0002\u0002\\V\u0003\u0002\u0002\u0002\\]\u0003",
    "\u0002\u0002\u0002]`\u0003\u0002\u0002\u0002^`\u0007\u000f\u0002\u0002",
    "_U\u0003\u0002\u0002\u0002_^\u0003\u0002\u0002\u0002`e\u0003\u0002\u0002",
    "\u0002ae\u0007\b\u0002\u0002be\u0005\u0016\f\u0002ce\u0005\u0018\r\u0002",
    "dM\u0003\u0002\u0002\u0002da\u0003\u0002\u0002\u0002db\u0003\u0002\u0002",
    "\u0002dc\u0003\u0002\u0002\u0002e\u000b\u0003\u0002\u0002\u0002fh\u0005",
    "\u0010\t\u0002gf\u0003\u0002\u0002\u0002gh\u0003\u0002\u0002\u0002h",
    "t\u0003\u0002\u0002\u0002in\u0005\n\u0006\u0002jn\u0007\u0004\u0002",
    "\u0002kn\u0007\u0007\u0002\u0002ln\u0005\u0014\u000b\u0002mi\u0003\u0002",
    "\u0002\u0002mj\u0003\u0002\u0002\u0002mk\u0003\u0002\u0002\u0002ml\u0003",
    "\u0002\u0002\u0002np\u0003\u0002\u0002\u0002oq\u0005\u0010\t\u0002p",
    "o\u0003\u0002\u0002\u0002pq\u0003\u0002\u0002\u0002qs\u0003\u0002\u0002",
    "\u0002rm\u0003\u0002\u0002\u0002sv\u0003\u0002\u0002\u0002tr\u0003\u0002",
    "\u0002\u0002tu\u0003\u0002\u0002\u0002u\r\u0003\u0002\u0002\u0002vt",
    "\u0003\u0002\u0002\u0002wz\u0007\u0012\u0002\u0002xy\u0007\u0011\u0002",
    "\u0002y{\u0007\u0018\u0002\u0002zx\u0003\u0002\u0002\u0002z{\u0003\u0002",
    "\u0002\u0002{\u000f\u0003\u0002\u0002\u0002|}\t\u0003\u0002\u0002}\u0011",
    "\u0003\u0002\u0002\u0002~\u0085\u0005\u0014\u000b\u0002\u007f\u0085",
    "\u0007\u0007\u0002\u0002\u0080\u0085\u0007\u0006\u0002\u0002\u0081\u0085",
    "\u0007\u0004\u0002\u0002\u0082\u0085\u0007\t\u0002\u0002\u0083\u0085",
    "\u0005\u0010\t\u0002\u0084~\u0003\u0002\u0002\u0002\u0084\u007f\u0003",
    "\u0002\u0002\u0002\u0084\u0080\u0003\u0002\u0002\u0002\u0084\u0081\u0003",
    "\u0002\u0002\u0002\u0084\u0082\u0003\u0002\u0002\u0002\u0084\u0083\u0003",
    "\u0002\u0002\u0002\u0085\u0013\u0003\u0002\u0002\u0002\u0086\u0087\t",
    "\u0004\u0002\u0002\u0087\u0015\u0003\u0002\u0002\u0002\u0088\u0089\u0007",
    "\n\u0002\u0002\u0089\u008a\t\u0005\u0002\u0002\u008a\u0017\u0003\u0002",
    "\u0002\u0002\u008b\u008c\u0007\u000b\u0002\u0002\u008c\u008d\t\u0006",
    "\u0002\u0002\u008d\u0019\u0003\u0002\u0002\u0002\u0015\u001d#\',05;",
    "CJR\\_dgmptz\u0084"].join("");


var atn = new antlr4.atn.ATNDeserializer().deserialize(serializedATN);

var decisionsToDFA = atn.decisionToState.map( function(ds, index) { return new antlr4.dfa.DFA(ds, index); });

var sharedContextCache = new antlr4.PredictionContextCache();

var literalNames = [ null, null, null, null, null, null, null, null, null, 
                     null, "'<'", null, "'>'", "'/>'", "'/'", "'='" ];

var symbolicNames = [ null, "HTML_COMMENT", "CDATA", "HTML_CONDITIONAL_COMMENT", 
                      "XML", "DTD", "SCRIPTLET", "SEA_WS", "SCRIPT_OPEN", 
                      "STYLE_OPEN", "TAG_OPEN", "HTML_TEXT", "TAG_CLOSE", 
                      "TAG_SLASH_CLOSE", "TAG_SLASH", "TAG_EQUALS", "TAG_NAME", 
                      "TAG_WHITESPACE", "SCRIPT_BODY", "SCRIPT_SHORT_BODY", 
                      "STYLE_BODY", "STYLE_SHORT_BODY", "ATTVALUE_VALUE", 
                      "ATTRIBUTE" ];

var ruleNames =  [ "html", "htmlDocument", "scriptletOrSeaWs", "htmlElements", 
                   "htmlElement", "htmlContent", "htmlAttribute", "htmlChardata", 
                   "htmlMisc", "htmlComment", "script", "style" ];

function HTMLParser (input) {
	antlr4.Parser.call(this, input);
    this._interp = new antlr4.atn.ParserATNSimulator(this, atn, decisionsToDFA, sharedContextCache);
    this.ruleNames = ruleNames;
    this.literalNames = literalNames;
    this.symbolicNames = symbolicNames;
    return this;
}

HTMLParser.prototype = Object.create(antlr4.Parser.prototype);
HTMLParser.prototype.constructor = HTMLParser;

Object.defineProperty(HTMLParser.prototype, "atn", {
	get : function() {
		return atn;
	}
});

HTMLParser.EOF = antlr4.Token.EOF;
HTMLParser.HTML_COMMENT = 1;
HTMLParser.CDATA = 2;
HTMLParser.HTML_CONDITIONAL_COMMENT = 3;
HTMLParser.XML = 4;
HTMLParser.DTD = 5;
HTMLParser.SCRIPTLET = 6;
HTMLParser.SEA_WS = 7;
HTMLParser.SCRIPT_OPEN = 8;
HTMLParser.STYLE_OPEN = 9;
HTMLParser.TAG_OPEN = 10;
HTMLParser.HTML_TEXT = 11;
HTMLParser.TAG_CLOSE = 12;
HTMLParser.TAG_SLASH_CLOSE = 13;
HTMLParser.TAG_SLASH = 14;
HTMLParser.TAG_EQUALS = 15;
HTMLParser.TAG_NAME = 16;
HTMLParser.TAG_WHITESPACE = 17;
HTMLParser.SCRIPT_BODY = 18;
HTMLParser.SCRIPT_SHORT_BODY = 19;
HTMLParser.STYLE_BODY = 20;
HTMLParser.STYLE_SHORT_BODY = 21;
HTMLParser.ATTVALUE_VALUE = 22;
HTMLParser.ATTRIBUTE = 23;

HTMLParser.RULE_html = 0;
HTMLParser.RULE_htmlDocument = 1;
HTMLParser.RULE_scriptletOrSeaWs = 2;
HTMLParser.RULE_htmlElements = 3;
HTMLParser.RULE_htmlElement = 4;
HTMLParser.RULE_htmlContent = 5;
HTMLParser.RULE_htmlAttribute = 6;
HTMLParser.RULE_htmlChardata = 7;
HTMLParser.RULE_htmlMisc = 8;
HTMLParser.RULE_htmlComment = 9;
HTMLParser.RULE_script = 10;
HTMLParser.RULE_style = 11;


function HtmlContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_html;
    return this;
}

HtmlContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlContext.prototype.constructor = HtmlContext;

HtmlContext.prototype.htmlElements = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlElementsContext);
    } else {
        return this.getTypedRuleContext(HtmlElementsContext,i);
    }
};




HTMLParser.HtmlContext = HtmlContext;

HTMLParser.prototype.html = function() {

    var localctx = new HtmlContext(this, this._ctx, this.state);
    this.enterRule(localctx, 0, HTMLParser.RULE_html);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 27;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        while((((_la) & ~0x1f) == 0 && ((1 << _la) & ((1 << HTMLParser.HTML_COMMENT) | (1 << HTMLParser.CDATA) | (1 << HTMLParser.HTML_CONDITIONAL_COMMENT) | (1 << HTMLParser.XML) | (1 << HTMLParser.DTD) | (1 << HTMLParser.SCRIPTLET) | (1 << HTMLParser.SEA_WS) | (1 << HTMLParser.SCRIPT_OPEN) | (1 << HTMLParser.STYLE_OPEN) | (1 << HTMLParser.TAG_OPEN) | (1 << HTMLParser.HTML_TEXT))) !== 0)) {
            this.state = 24;
            this.htmlElements();
            this.state = 29;
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlDocumentContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlDocument;
    return this;
}

HtmlDocumentContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlDocumentContext.prototype.constructor = HtmlDocumentContext;

HtmlDocumentContext.prototype.scriptletOrSeaWs = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(ScriptletOrSeaWsContext);
    } else {
        return this.getTypedRuleContext(ScriptletOrSeaWsContext,i);
    }
};

HtmlDocumentContext.prototype.XML = function() {
    return this.getToken(HTMLParser.XML, 0);
};

HtmlDocumentContext.prototype.DTD = function() {
    return this.getToken(HTMLParser.DTD, 0);
};

HtmlDocumentContext.prototype.htmlElements = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlElementsContext);
    } else {
        return this.getTypedRuleContext(HtmlElementsContext,i);
    }
};




HTMLParser.HtmlDocumentContext = HtmlDocumentContext;

HTMLParser.prototype.htmlDocument = function() {

    var localctx = new HtmlDocumentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 2, HTMLParser.RULE_htmlDocument);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 33;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,1,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 30;
                this.scriptletOrSeaWs(); 
            }
            this.state = 35;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,1,this._ctx);
        }

        this.state = 37;
        this._errHandler.sync(this);
        var la_ = this._interp.adaptivePredict(this._input,2,this._ctx);
        if(la_===1) {
            this.state = 36;
            this.match(HTMLParser.XML);

        }
        this.state = 42;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,3,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 39;
                this.scriptletOrSeaWs(); 
            }
            this.state = 44;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,3,this._ctx);
        }

        this.state = 46;
        this._errHandler.sync(this);
        var la_ = this._interp.adaptivePredict(this._input,4,this._ctx);
        if(la_===1) {
            this.state = 45;
            this.match(HTMLParser.DTD);

        }
        this.state = 51;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,5,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 48;
                this.scriptletOrSeaWs(); 
            }
            this.state = 53;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,5,this._ctx);
        }

        this.state = 57;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        while((((_la) & ~0x1f) == 0 && ((1 << _la) & ((1 << HTMLParser.HTML_COMMENT) | (1 << HTMLParser.CDATA) | (1 << HTMLParser.HTML_CONDITIONAL_COMMENT) | (1 << HTMLParser.XML) | (1 << HTMLParser.DTD) | (1 << HTMLParser.SCRIPTLET) | (1 << HTMLParser.SEA_WS) | (1 << HTMLParser.SCRIPT_OPEN) | (1 << HTMLParser.STYLE_OPEN) | (1 << HTMLParser.TAG_OPEN) | (1 << HTMLParser.HTML_TEXT))) !== 0)) {
            this.state = 54;
            this.htmlElements();
            this.state = 59;
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function ScriptletOrSeaWsContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_scriptletOrSeaWs;
    return this;
}

ScriptletOrSeaWsContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
ScriptletOrSeaWsContext.prototype.constructor = ScriptletOrSeaWsContext;

ScriptletOrSeaWsContext.prototype.SCRIPTLET = function() {
    return this.getToken(HTMLParser.SCRIPTLET, 0);
};

ScriptletOrSeaWsContext.prototype.SEA_WS = function() {
    return this.getToken(HTMLParser.SEA_WS, 0);
};




HTMLParser.ScriptletOrSeaWsContext = ScriptletOrSeaWsContext;

HTMLParser.prototype.scriptletOrSeaWs = function() {

    var localctx = new ScriptletOrSeaWsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 4, HTMLParser.RULE_scriptletOrSeaWs);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 60;
        _la = this._input.LA(1);
        if(!(_la===HTMLParser.SCRIPTLET || _la===HTMLParser.SEA_WS)) {
        this._errHandler.recoverInline(this);
        }
        else {
        	this._errHandler.reportMatch(this);
            this.consume();
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlElementsContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlElements;
    return this;
}

HtmlElementsContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlElementsContext.prototype.constructor = HtmlElementsContext;

HtmlElementsContext.prototype.htmlElement = function() {
    return this.getTypedRuleContext(HtmlElementContext,0);
};

HtmlElementsContext.prototype.htmlMisc = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlMiscContext);
    } else {
        return this.getTypedRuleContext(HtmlMiscContext,i);
    }
};




HTMLParser.HtmlElementsContext = HtmlElementsContext;

HTMLParser.prototype.htmlElements = function() {

    var localctx = new HtmlElementsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 6, HTMLParser.RULE_htmlElements);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 65;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        while((((_la) & ~0x1f) == 0 && ((1 << _la) & ((1 << HTMLParser.HTML_COMMENT) | (1 << HTMLParser.CDATA) | (1 << HTMLParser.HTML_CONDITIONAL_COMMENT) | (1 << HTMLParser.XML) | (1 << HTMLParser.DTD) | (1 << HTMLParser.SEA_WS) | (1 << HTMLParser.HTML_TEXT))) !== 0)) {
            this.state = 62;
            this.htmlMisc();
            this.state = 67;
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        }
        this.state = 68;
        this.htmlElement();
        this.state = 72;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,8,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 69;
                this.htmlMisc(); 
            }
            this.state = 74;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,8,this._ctx);
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlElementContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlElement;
    return this;
}

HtmlElementContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlElementContext.prototype.constructor = HtmlElementContext;

HtmlElementContext.prototype.TAG_OPEN = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(HTMLParser.TAG_OPEN);
    } else {
        return this.getToken(HTMLParser.TAG_OPEN, i);
    }
};


HtmlElementContext.prototype.TAG_NAME = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(HTMLParser.TAG_NAME);
    } else {
        return this.getToken(HTMLParser.TAG_NAME, i);
    }
};


HtmlElementContext.prototype.TAG_CLOSE = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(HTMLParser.TAG_CLOSE);
    } else {
        return this.getToken(HTMLParser.TAG_CLOSE, i);
    }
};


HtmlElementContext.prototype.TAG_SLASH_CLOSE = function() {
    return this.getToken(HTMLParser.TAG_SLASH_CLOSE, 0);
};

HtmlElementContext.prototype.htmlAttribute = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlAttributeContext);
    } else {
        return this.getTypedRuleContext(HtmlAttributeContext,i);
    }
};

HtmlElementContext.prototype.htmlContent = function() {
    return this.getTypedRuleContext(HtmlContentContext,0);
};

HtmlElementContext.prototype.TAG_SLASH = function() {
    return this.getToken(HTMLParser.TAG_SLASH, 0);
};

HtmlElementContext.prototype.SCRIPTLET = function() {
    return this.getToken(HTMLParser.SCRIPTLET, 0);
};

HtmlElementContext.prototype.script = function() {
    return this.getTypedRuleContext(ScriptContext,0);
};

HtmlElementContext.prototype.style = function() {
    return this.getTypedRuleContext(StyleContext,0);
};




HTMLParser.HtmlElementContext = HtmlElementContext;

HTMLParser.prototype.htmlElement = function() {

    var localctx = new HtmlElementContext(this, this._ctx, this.state);
    this.enterRule(localctx, 8, HTMLParser.RULE_htmlElement);
    var _la = 0; // Token type
    try {
        this.state = 98;
        this._errHandler.sync(this);
        switch(this._input.LA(1)) {
        case HTMLParser.TAG_OPEN:
            this.enterOuterAlt(localctx, 1);
            this.state = 75;
            this.match(HTMLParser.TAG_OPEN);
            this.state = 76;
            this.match(HTMLParser.TAG_NAME);
            this.state = 80;
            this._errHandler.sync(this);
            _la = this._input.LA(1);
            while(_la===HTMLParser.TAG_NAME) {
                this.state = 77;
                this.htmlAttribute();
                this.state = 82;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
            }
            this.state = 93;
            this._errHandler.sync(this);
            switch(this._input.LA(1)) {
            case HTMLParser.TAG_CLOSE:
                this.state = 83;
                this.match(HTMLParser.TAG_CLOSE);
                this.state = 90;
                this._errHandler.sync(this);
                var la_ = this._interp.adaptivePredict(this._input,10,this._ctx);
                if(la_===1) {
                    this.state = 84;
                    this.htmlContent();
                    this.state = 85;
                    this.match(HTMLParser.TAG_OPEN);
                    this.state = 86;
                    this.match(HTMLParser.TAG_SLASH);
                    this.state = 87;
                    this.match(HTMLParser.TAG_NAME);
                    this.state = 88;
                    this.match(HTMLParser.TAG_CLOSE);

                }
                break;
            case HTMLParser.TAG_SLASH_CLOSE:
                this.state = 92;
                this.match(HTMLParser.TAG_SLASH_CLOSE);
                break;
            default:
                throw new antlr4.error.NoViableAltException(this);
            }
            break;
        case HTMLParser.SCRIPTLET:
            this.enterOuterAlt(localctx, 2);
            this.state = 95;
            this.match(HTMLParser.SCRIPTLET);
            break;
        case HTMLParser.SCRIPT_OPEN:
            this.enterOuterAlt(localctx, 3);
            this.state = 96;
            this.script();
            break;
        case HTMLParser.STYLE_OPEN:
            this.enterOuterAlt(localctx, 4);
            this.state = 97;
            this.style();
            break;
        default:
            throw new antlr4.error.NoViableAltException(this);
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlContentContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlContent;
    return this;
}

HtmlContentContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlContentContext.prototype.constructor = HtmlContentContext;

HtmlContentContext.prototype.htmlChardata = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlChardataContext);
    } else {
        return this.getTypedRuleContext(HtmlChardataContext,i);
    }
};

HtmlContentContext.prototype.htmlElement = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlElementContext);
    } else {
        return this.getTypedRuleContext(HtmlElementContext,i);
    }
};

HtmlContentContext.prototype.CDATA = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(HTMLParser.CDATA);
    } else {
        return this.getToken(HTMLParser.CDATA, i);
    }
};


HtmlContentContext.prototype.DTD = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(HTMLParser.DTD);
    } else {
        return this.getToken(HTMLParser.DTD, i);
    }
};


HtmlContentContext.prototype.htmlComment = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(HtmlCommentContext);
    } else {
        return this.getTypedRuleContext(HtmlCommentContext,i);
    }
};




HTMLParser.HtmlContentContext = HtmlContentContext;

HTMLParser.prototype.htmlContent = function() {

    var localctx = new HtmlContentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 10, HTMLParser.RULE_htmlContent);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 101;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===HTMLParser.SEA_WS || _la===HTMLParser.HTML_TEXT) {
            this.state = 100;
            this.htmlChardata();
        }

        this.state = 114;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,16,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 107;
                this._errHandler.sync(this);
                switch(this._input.LA(1)) {
                case HTMLParser.SCRIPTLET:
                case HTMLParser.SCRIPT_OPEN:
                case HTMLParser.STYLE_OPEN:
                case HTMLParser.TAG_OPEN:
                    this.state = 103;
                    this.htmlElement();
                    break;
                case HTMLParser.CDATA:
                    this.state = 104;
                    this.match(HTMLParser.CDATA);
                    break;
                case HTMLParser.DTD:
                    this.state = 105;
                    this.match(HTMLParser.DTD);
                    break;
                case HTMLParser.HTML_COMMENT:
                case HTMLParser.HTML_CONDITIONAL_COMMENT:
                    this.state = 106;
                    this.htmlComment();
                    break;
                default:
                    throw new antlr4.error.NoViableAltException(this);
                }
                this.state = 110;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if(_la===HTMLParser.SEA_WS || _la===HTMLParser.HTML_TEXT) {
                    this.state = 109;
                    this.htmlChardata();
                }
         
            }
            this.state = 116;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,16,this._ctx);
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlAttributeContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlAttribute;
    return this;
}

HtmlAttributeContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlAttributeContext.prototype.constructor = HtmlAttributeContext;

HtmlAttributeContext.prototype.TAG_NAME = function() {
    return this.getToken(HTMLParser.TAG_NAME, 0);
};

HtmlAttributeContext.prototype.TAG_EQUALS = function() {
    return this.getToken(HTMLParser.TAG_EQUALS, 0);
};

HtmlAttributeContext.prototype.ATTVALUE_VALUE = function() {
    return this.getToken(HTMLParser.ATTVALUE_VALUE, 0);
};




HTMLParser.HtmlAttributeContext = HtmlAttributeContext;

HTMLParser.prototype.htmlAttribute = function() {

    var localctx = new HtmlAttributeContext(this, this._ctx, this.state);
    this.enterRule(localctx, 12, HTMLParser.RULE_htmlAttribute);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 117;
        this.match(HTMLParser.TAG_NAME);
        this.state = 120;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===HTMLParser.TAG_EQUALS) {
            this.state = 118;
            this.match(HTMLParser.TAG_EQUALS);
            this.state = 119;
            this.match(HTMLParser.ATTVALUE_VALUE);
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlChardataContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlChardata;
    return this;
}

HtmlChardataContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlChardataContext.prototype.constructor = HtmlChardataContext;

HtmlChardataContext.prototype.HTML_TEXT = function() {
    return this.getToken(HTMLParser.HTML_TEXT, 0);
};

HtmlChardataContext.prototype.SEA_WS = function() {
    return this.getToken(HTMLParser.SEA_WS, 0);
};




HTMLParser.HtmlChardataContext = HtmlChardataContext;

HTMLParser.prototype.htmlChardata = function() {

    var localctx = new HtmlChardataContext(this, this._ctx, this.state);
    this.enterRule(localctx, 14, HTMLParser.RULE_htmlChardata);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 122;
        _la = this._input.LA(1);
        if(!(_la===HTMLParser.SEA_WS || _la===HTMLParser.HTML_TEXT)) {
        this._errHandler.recoverInline(this);
        }
        else {
        	this._errHandler.reportMatch(this);
            this.consume();
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlMiscContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlMisc;
    return this;
}

HtmlMiscContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlMiscContext.prototype.constructor = HtmlMiscContext;

HtmlMiscContext.prototype.htmlComment = function() {
    return this.getTypedRuleContext(HtmlCommentContext,0);
};

HtmlMiscContext.prototype.DTD = function() {
    return this.getToken(HTMLParser.DTD, 0);
};

HtmlMiscContext.prototype.XML = function() {
    return this.getToken(HTMLParser.XML, 0);
};

HtmlMiscContext.prototype.CDATA = function() {
    return this.getToken(HTMLParser.CDATA, 0);
};

HtmlMiscContext.prototype.SEA_WS = function() {
    return this.getToken(HTMLParser.SEA_WS, 0);
};

HtmlMiscContext.prototype.htmlChardata = function() {
    return this.getTypedRuleContext(HtmlChardataContext,0);
};




HTMLParser.HtmlMiscContext = HtmlMiscContext;

HTMLParser.prototype.htmlMisc = function() {

    var localctx = new HtmlMiscContext(this, this._ctx, this.state);
    this.enterRule(localctx, 16, HTMLParser.RULE_htmlMisc);
    try {
        this.state = 130;
        this._errHandler.sync(this);
        var la_ = this._interp.adaptivePredict(this._input,18,this._ctx);
        switch(la_) {
        case 1:
            this.enterOuterAlt(localctx, 1);
            this.state = 124;
            this.htmlComment();
            break;

        case 2:
            this.enterOuterAlt(localctx, 2);
            this.state = 125;
            this.match(HTMLParser.DTD);
            break;

        case 3:
            this.enterOuterAlt(localctx, 3);
            this.state = 126;
            this.match(HTMLParser.XML);
            break;

        case 4:
            this.enterOuterAlt(localctx, 4);
            this.state = 127;
            this.match(HTMLParser.CDATA);
            break;

        case 5:
            this.enterOuterAlt(localctx, 5);
            this.state = 128;
            this.match(HTMLParser.SEA_WS);
            break;

        case 6:
            this.enterOuterAlt(localctx, 6);
            this.state = 129;
            this.htmlChardata();
            break;

        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function HtmlCommentContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_htmlComment;
    return this;
}

HtmlCommentContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
HtmlCommentContext.prototype.constructor = HtmlCommentContext;

HtmlCommentContext.prototype.HTML_COMMENT = function() {
    return this.getToken(HTMLParser.HTML_COMMENT, 0);
};

HtmlCommentContext.prototype.HTML_CONDITIONAL_COMMENT = function() {
    return this.getToken(HTMLParser.HTML_CONDITIONAL_COMMENT, 0);
};




HTMLParser.HtmlCommentContext = HtmlCommentContext;

HTMLParser.prototype.htmlComment = function() {

    var localctx = new HtmlCommentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 18, HTMLParser.RULE_htmlComment);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 132;
        _la = this._input.LA(1);
        if(!(_la===HTMLParser.HTML_COMMENT || _la===HTMLParser.HTML_CONDITIONAL_COMMENT)) {
        this._errHandler.recoverInline(this);
        }
        else {
        	this._errHandler.reportMatch(this);
            this.consume();
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function ScriptContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_script;
    return this;
}

ScriptContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
ScriptContext.prototype.constructor = ScriptContext;

ScriptContext.prototype.SCRIPT_OPEN = function() {
    return this.getToken(HTMLParser.SCRIPT_OPEN, 0);
};

ScriptContext.prototype.SCRIPT_BODY = function() {
    return this.getToken(HTMLParser.SCRIPT_BODY, 0);
};

ScriptContext.prototype.SCRIPT_SHORT_BODY = function() {
    return this.getToken(HTMLParser.SCRIPT_SHORT_BODY, 0);
};




HTMLParser.ScriptContext = ScriptContext;

HTMLParser.prototype.script = function() {

    var localctx = new ScriptContext(this, this._ctx, this.state);
    this.enterRule(localctx, 20, HTMLParser.RULE_script);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 134;
        this.match(HTMLParser.SCRIPT_OPEN);
        this.state = 135;
        _la = this._input.LA(1);
        if(!(_la===HTMLParser.SCRIPT_BODY || _la===HTMLParser.SCRIPT_SHORT_BODY)) {
        this._errHandler.recoverInline(this);
        }
        else {
        	this._errHandler.reportMatch(this);
            this.consume();
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


function StyleContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = HTMLParser.RULE_style;
    return this;
}

StyleContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
StyleContext.prototype.constructor = StyleContext;

StyleContext.prototype.STYLE_OPEN = function() {
    return this.getToken(HTMLParser.STYLE_OPEN, 0);
};

StyleContext.prototype.STYLE_BODY = function() {
    return this.getToken(HTMLParser.STYLE_BODY, 0);
};

StyleContext.prototype.STYLE_SHORT_BODY = function() {
    return this.getToken(HTMLParser.STYLE_SHORT_BODY, 0);
};




HTMLParser.StyleContext = StyleContext;

HTMLParser.prototype.style = function() {

    var localctx = new StyleContext(this, this._ctx, this.state);
    this.enterRule(localctx, 22, HTMLParser.RULE_style);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 137;
        this.match(HTMLParser.STYLE_OPEN);
        this.state = 138;
        _la = this._input.LA(1);
        if(!(_la===HTMLParser.STYLE_BODY || _la===HTMLParser.STYLE_SHORT_BODY)) {
        this._errHandler.recoverInline(this);
        }
        else {
        	this._errHandler.reportMatch(this);
            this.consume();
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


exports.HTMLParser = HTMLParser;
