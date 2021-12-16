// Generated from /Users/sansarip/Dev/owlbear/antlr4/HTMLParser.g4 by ANTLR 4.8
// jshint ignore: start
var antlr4 = require('antlr4/index');
var HTMLParserVisitor = require('./HTMLParserVisitor').HTMLParserVisitor;

var grammarFileName = "HTMLParser.g4";


var serializedATN = ["\u0003\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964",
    "\u0003\u0019\u0082\u0004\u0002\t\u0002\u0004\u0003\t\u0003\u0004\u0004",
    "\t\u0004\u0004\u0005\t\u0005\u0004\u0006\t\u0006\u0004\u0007\t\u0007",
    "\u0004\b\t\b\u0004\t\t\t\u0004\n\t\n\u0004\u000b\t\u000b\u0004\f\t\f",
    "\u0003\u0002\u0007\u0002\u001a\n\u0002\f\u0002\u000e\u0002\u001d\u000b",
    "\u0002\u0003\u0002\u0005\u0002 \n\u0002\u0003\u0002\u0007\u0002#\n\u0002",
    "\f\u0002\u000e\u0002&\u000b\u0002\u0003\u0002\u0005\u0002)\n\u0002\u0003",
    "\u0002\u0007\u0002,\n\u0002\f\u0002\u000e\u0002/\u000b\u0002\u0003\u0002",
    "\u0007\u00022\n\u0002\f\u0002\u000e\u00025\u000b\u0002\u0003\u0003\u0003",
    "\u0003\u0003\u0004\u0007\u0004:\n\u0004\f\u0004\u000e\u0004=\u000b\u0004",
    "\u0003\u0004\u0003\u0004\u0007\u0004A\n\u0004\f\u0004\u000e\u0004D\u000b",
    "\u0004\u0003\u0005\u0003\u0005\u0003\u0005\u0007\u0005I\n\u0005\f\u0005",
    "\u000e\u0005L\u000b\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003",
    "\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0005\u0005U\n\u0005\u0003",
    "\u0005\u0005\u0005X\n\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0005",
    "\u0005]\n\u0005\u0003\u0006\u0005\u0006`\n\u0006\u0003\u0006\u0003\u0006",
    "\u0003\u0006\u0005\u0006e\n\u0006\u0003\u0006\u0005\u0006h\n\u0006\u0007",
    "\u0006j\n\u0006\f\u0006\u000e\u0006m\u000b\u0006\u0003\u0007\u0003\u0007",
    "\u0003\u0007\u0005\u0007r\n\u0007\u0003\b\u0003\b\u0003\t\u0003\t\u0005",
    "\tx\n\t\u0003\n\u0003\n\u0003\u000b\u0003\u000b\u0003\u000b\u0003\f",
    "\u0003\f\u0003\f\u0003\f\u0002\u0002\r\u0002\u0004\u0006\b\n\f\u000e",
    "\u0010\u0012\u0014\u0016\u0002\u0007\u0003\u0002\b\t\u0004\u0002\t\t",
    "\r\r\u0003\u0002\u0003\u0004\u0003\u0002\u0014\u0015\u0003\u0002\u0016",
    "\u0017\u0002\u008b\u0002\u001b\u0003\u0002\u0002\u0002\u00046\u0003",
    "\u0002\u0002\u0002\u0006;\u0003\u0002\u0002\u0002\b\\\u0003\u0002\u0002",
    "\u0002\n_\u0003\u0002\u0002\u0002\fn\u0003\u0002\u0002\u0002\u000es",
    "\u0003\u0002\u0002\u0002\u0010w\u0003\u0002\u0002\u0002\u0012y\u0003",
    "\u0002\u0002\u0002\u0014{\u0003\u0002\u0002\u0002\u0016~\u0003\u0002",
    "\u0002\u0002\u0018\u001a\u0005\u0004\u0003\u0002\u0019\u0018\u0003\u0002",
    "\u0002\u0002\u001a\u001d\u0003\u0002\u0002\u0002\u001b\u0019\u0003\u0002",
    "\u0002\u0002\u001b\u001c\u0003\u0002\u0002\u0002\u001c\u001f\u0003\u0002",
    "\u0002\u0002\u001d\u001b\u0003\u0002\u0002\u0002\u001e \u0007\u0005",
    "\u0002\u0002\u001f\u001e\u0003\u0002\u0002\u0002\u001f \u0003\u0002",
    "\u0002\u0002 $\u0003\u0002\u0002\u0002!#\u0005\u0004\u0003\u0002\"!",
    "\u0003\u0002\u0002\u0002#&\u0003\u0002\u0002\u0002$\"\u0003\u0002\u0002",
    "\u0002$%\u0003\u0002\u0002\u0002%(\u0003\u0002\u0002\u0002&$\u0003\u0002",
    "\u0002\u0002\')\u0007\u0007\u0002\u0002(\'\u0003\u0002\u0002\u0002(",
    ")\u0003\u0002\u0002\u0002)-\u0003\u0002\u0002\u0002*,\u0005\u0004\u0003",
    "\u0002+*\u0003\u0002\u0002\u0002,/\u0003\u0002\u0002\u0002-+\u0003\u0002",
    "\u0002\u0002-.\u0003\u0002\u0002\u0002.3\u0003\u0002\u0002\u0002/-\u0003",
    "\u0002\u0002\u000202\u0005\u0006\u0004\u000210\u0003\u0002\u0002\u0002",
    "25\u0003\u0002\u0002\u000231\u0003\u0002\u0002\u000234\u0003\u0002\u0002",
    "\u00024\u0003\u0003\u0002\u0002\u000253\u0003\u0002\u0002\u000267\t",
    "\u0002\u0002\u00027\u0005\u0003\u0002\u0002\u00028:\u0005\u0010\t\u0002",
    "98\u0003\u0002\u0002\u0002:=\u0003\u0002\u0002\u0002;9\u0003\u0002\u0002",
    "\u0002;<\u0003\u0002\u0002\u0002<>\u0003\u0002\u0002\u0002=;\u0003\u0002",
    "\u0002\u0002>B\u0005\b\u0005\u0002?A\u0005\u0010\t\u0002@?\u0003\u0002",
    "\u0002\u0002AD\u0003\u0002\u0002\u0002B@\u0003\u0002\u0002\u0002BC\u0003",
    "\u0002\u0002\u0002C\u0007\u0003\u0002\u0002\u0002DB\u0003\u0002\u0002",
    "\u0002EF\u0007\f\u0002\u0002FJ\u0007\u0012\u0002\u0002GI\u0005\f\u0007",
    "\u0002HG\u0003\u0002\u0002\u0002IL\u0003\u0002\u0002\u0002JH\u0003\u0002",
    "\u0002\u0002JK\u0003\u0002\u0002\u0002KW\u0003\u0002\u0002\u0002LJ\u0003",
    "\u0002\u0002\u0002MT\u0007\u000e\u0002\u0002NO\u0005\n\u0006\u0002O",
    "P\u0007\f\u0002\u0002PQ\u0007\u0010\u0002\u0002QR\u0007\u0012\u0002",
    "\u0002RS\u0007\u000e\u0002\u0002SU\u0003\u0002\u0002\u0002TN\u0003\u0002",
    "\u0002\u0002TU\u0003\u0002\u0002\u0002UX\u0003\u0002\u0002\u0002VX\u0007",
    "\u000f\u0002\u0002WM\u0003\u0002\u0002\u0002WV\u0003\u0002\u0002\u0002",
    "X]\u0003\u0002\u0002\u0002Y]\u0007\b\u0002\u0002Z]\u0005\u0014\u000b",
    "\u0002[]\u0005\u0016\f\u0002\\E\u0003\u0002\u0002\u0002\\Y\u0003\u0002",
    "\u0002\u0002\\Z\u0003\u0002\u0002\u0002\\[\u0003\u0002\u0002\u0002]",
    "\t\u0003\u0002\u0002\u0002^`\u0005\u000e\b\u0002_^\u0003\u0002\u0002",
    "\u0002_`\u0003\u0002\u0002\u0002`k\u0003\u0002\u0002\u0002ae\u0005\b",
    "\u0005\u0002be\u0007\u0006\u0002\u0002ce\u0005\u0012\n\u0002da\u0003",
    "\u0002\u0002\u0002db\u0003\u0002\u0002\u0002dc\u0003\u0002\u0002\u0002",
    "eg\u0003\u0002\u0002\u0002fh\u0005\u000e\b\u0002gf\u0003\u0002\u0002",
    "\u0002gh\u0003\u0002\u0002\u0002hj\u0003\u0002\u0002\u0002id\u0003\u0002",
    "\u0002\u0002jm\u0003\u0002\u0002\u0002ki\u0003\u0002\u0002\u0002kl\u0003",
    "\u0002\u0002\u0002l\u000b\u0003\u0002\u0002\u0002mk\u0003\u0002\u0002",
    "\u0002nq\u0007\u0012\u0002\u0002op\u0007\u0011\u0002\u0002pr\u0007\u0018",
    "\u0002\u0002qo\u0003\u0002\u0002\u0002qr\u0003\u0002\u0002\u0002r\r",
    "\u0003\u0002\u0002\u0002st\t\u0003\u0002\u0002t\u000f\u0003\u0002\u0002",
    "\u0002ux\u0005\u0012\n\u0002vx\u0007\t\u0002\u0002wu\u0003\u0002\u0002",
    "\u0002wv\u0003\u0002\u0002\u0002x\u0011\u0003\u0002\u0002\u0002yz\t",
    "\u0004\u0002\u0002z\u0013\u0003\u0002\u0002\u0002{|\u0007\n\u0002\u0002",
    "|}\t\u0005\u0002\u0002}\u0015\u0003\u0002\u0002\u0002~\u007f\u0007\u000b",
    "\u0002\u0002\u007f\u0080\t\u0006\u0002\u0002\u0080\u0017\u0003\u0002",
    "\u0002\u0002\u0014\u001b\u001f$(-3;BJTW\\_dgkqw"].join("");


var atn = new antlr4.atn.ATNDeserializer().deserialize(serializedATN);

var decisionsToDFA = atn.decisionToState.map( function(ds, index) { return new antlr4.dfa.DFA(ds, index); });

var sharedContextCache = new antlr4.PredictionContextCache();

var literalNames = [ null, null, null, null, null, null, null, null, null, 
                     null, "'<'", null, "'>'", "'/>'", "'/'", "'='" ];

var symbolicNames = [ null, "HTML_COMMENT", "HTML_CONDITIONAL_COMMENT", 
                      "XML", "CDATA", "DTD", "SCRIPTLET", "SEA_WS", "SCRIPT_OPEN", 
                      "STYLE_OPEN", "TAG_OPEN", "HTML_TEXT", "TAG_CLOSE", 
                      "TAG_SLASH_CLOSE", "TAG_SLASH", "TAG_EQUALS", "TAG_NAME", 
                      "TAG_WHITESPACE", "SCRIPT_BODY", "SCRIPT_SHORT_BODY", 
                      "STYLE_BODY", "STYLE_SHORT_BODY", "ATTVALUE_VALUE", 
                      "ATTRIBUTE" ];

var ruleNames =  [ "htmlDocument", "scriptletOrSeaWs", "htmlElements", "htmlElement", 
                   "htmlContent", "htmlAttribute", "htmlChardata", "htmlMisc", 
                   "htmlComment", "script", "style" ];

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
HTMLParser.HTML_CONDITIONAL_COMMENT = 2;
HTMLParser.XML = 3;
HTMLParser.CDATA = 4;
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

HTMLParser.RULE_htmlDocument = 0;
HTMLParser.RULE_scriptletOrSeaWs = 1;
HTMLParser.RULE_htmlElements = 2;
HTMLParser.RULE_htmlElement = 3;
HTMLParser.RULE_htmlContent = 4;
HTMLParser.RULE_htmlAttribute = 5;
HTMLParser.RULE_htmlChardata = 6;
HTMLParser.RULE_htmlMisc = 7;
HTMLParser.RULE_htmlComment = 8;
HTMLParser.RULE_script = 9;
HTMLParser.RULE_style = 10;


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

HtmlDocumentContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlDocument(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlDocumentContext = HtmlDocumentContext;

HTMLParser.prototype.htmlDocument = function() {

    var localctx = new HtmlDocumentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 0, HTMLParser.RULE_htmlDocument);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 25;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,0,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 22;
                this.scriptletOrSeaWs(); 
            }
            this.state = 27;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,0,this._ctx);
        }

        this.state = 29;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===HTMLParser.XML) {
            this.state = 28;
            this.match(HTMLParser.XML);
        }

        this.state = 34;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,2,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 31;
                this.scriptletOrSeaWs(); 
            }
            this.state = 36;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,2,this._ctx);
        }

        this.state = 38;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===HTMLParser.DTD) {
            this.state = 37;
            this.match(HTMLParser.DTD);
        }

        this.state = 43;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,4,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 40;
                this.scriptletOrSeaWs(); 
            }
            this.state = 45;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,4,this._ctx);
        }

        this.state = 49;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        while((((_la) & ~0x1f) == 0 && ((1 << _la) & ((1 << HTMLParser.HTML_COMMENT) | (1 << HTMLParser.HTML_CONDITIONAL_COMMENT) | (1 << HTMLParser.SCRIPTLET) | (1 << HTMLParser.SEA_WS) | (1 << HTMLParser.SCRIPT_OPEN) | (1 << HTMLParser.STYLE_OPEN) | (1 << HTMLParser.TAG_OPEN))) !== 0)) {
            this.state = 46;
            this.htmlElements();
            this.state = 51;
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

ScriptletOrSeaWsContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitScriptletOrSeaWs(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.ScriptletOrSeaWsContext = ScriptletOrSeaWsContext;

HTMLParser.prototype.scriptletOrSeaWs = function() {

    var localctx = new ScriptletOrSeaWsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 2, HTMLParser.RULE_scriptletOrSeaWs);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 52;
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

HtmlElementsContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlElements(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlElementsContext = HtmlElementsContext;

HTMLParser.prototype.htmlElements = function() {

    var localctx = new HtmlElementsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 4, HTMLParser.RULE_htmlElements);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 57;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        while((((_la) & ~0x1f) == 0 && ((1 << _la) & ((1 << HTMLParser.HTML_COMMENT) | (1 << HTMLParser.HTML_CONDITIONAL_COMMENT) | (1 << HTMLParser.SEA_WS))) !== 0)) {
            this.state = 54;
            this.htmlMisc();
            this.state = 59;
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        }
        this.state = 60;
        this.htmlElement();
        this.state = 64;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,7,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 61;
                this.htmlMisc(); 
            }
            this.state = 66;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,7,this._ctx);
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

HtmlElementContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlElement(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlElementContext = HtmlElementContext;

HTMLParser.prototype.htmlElement = function() {

    var localctx = new HtmlElementContext(this, this._ctx, this.state);
    this.enterRule(localctx, 6, HTMLParser.RULE_htmlElement);
    var _la = 0; // Token type
    try {
        this.state = 90;
        this._errHandler.sync(this);
        switch(this._input.LA(1)) {
        case HTMLParser.TAG_OPEN:
            this.enterOuterAlt(localctx, 1);
            this.state = 67;
            this.match(HTMLParser.TAG_OPEN);
            this.state = 68;
            this.match(HTMLParser.TAG_NAME);
            this.state = 72;
            this._errHandler.sync(this);
            _la = this._input.LA(1);
            while(_la===HTMLParser.TAG_NAME) {
                this.state = 69;
                this.htmlAttribute();
                this.state = 74;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
            }
            this.state = 85;
            this._errHandler.sync(this);
            switch(this._input.LA(1)) {
            case HTMLParser.TAG_CLOSE:
                this.state = 75;
                this.match(HTMLParser.TAG_CLOSE);
                this.state = 82;
                this._errHandler.sync(this);
                var la_ = this._interp.adaptivePredict(this._input,9,this._ctx);
                if(la_===1) {
                    this.state = 76;
                    this.htmlContent();
                    this.state = 77;
                    this.match(HTMLParser.TAG_OPEN);
                    this.state = 78;
                    this.match(HTMLParser.TAG_SLASH);
                    this.state = 79;
                    this.match(HTMLParser.TAG_NAME);
                    this.state = 80;
                    this.match(HTMLParser.TAG_CLOSE);

                }
                break;
            case HTMLParser.TAG_SLASH_CLOSE:
                this.state = 84;
                this.match(HTMLParser.TAG_SLASH_CLOSE);
                break;
            default:
                throw new antlr4.error.NoViableAltException(this);
            }
            break;
        case HTMLParser.SCRIPTLET:
            this.enterOuterAlt(localctx, 2);
            this.state = 87;
            this.match(HTMLParser.SCRIPTLET);
            break;
        case HTMLParser.SCRIPT_OPEN:
            this.enterOuterAlt(localctx, 3);
            this.state = 88;
            this.script();
            break;
        case HTMLParser.STYLE_OPEN:
            this.enterOuterAlt(localctx, 4);
            this.state = 89;
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

HtmlContentContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlContent(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlContentContext = HtmlContentContext;

HTMLParser.prototype.htmlContent = function() {

    var localctx = new HtmlContentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 8, HTMLParser.RULE_htmlContent);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 93;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===HTMLParser.SEA_WS || _la===HTMLParser.HTML_TEXT) {
            this.state = 92;
            this.htmlChardata();
        }

        this.state = 105;
        this._errHandler.sync(this);
        var _alt = this._interp.adaptivePredict(this._input,15,this._ctx)
        while(_alt!=2 && _alt!=antlr4.atn.ATN.INVALID_ALT_NUMBER) {
            if(_alt===1) {
                this.state = 98;
                this._errHandler.sync(this);
                switch(this._input.LA(1)) {
                case HTMLParser.SCRIPTLET:
                case HTMLParser.SCRIPT_OPEN:
                case HTMLParser.STYLE_OPEN:
                case HTMLParser.TAG_OPEN:
                    this.state = 95;
                    this.htmlElement();
                    break;
                case HTMLParser.CDATA:
                    this.state = 96;
                    this.match(HTMLParser.CDATA);
                    break;
                case HTMLParser.HTML_COMMENT:
                case HTMLParser.HTML_CONDITIONAL_COMMENT:
                    this.state = 97;
                    this.htmlComment();
                    break;
                default:
                    throw new antlr4.error.NoViableAltException(this);
                }
                this.state = 101;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if(_la===HTMLParser.SEA_WS || _la===HTMLParser.HTML_TEXT) {
                    this.state = 100;
                    this.htmlChardata();
                }
         
            }
            this.state = 107;
            this._errHandler.sync(this);
            _alt = this._interp.adaptivePredict(this._input,15,this._ctx);
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

HtmlAttributeContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlAttribute(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlAttributeContext = HtmlAttributeContext;

HTMLParser.prototype.htmlAttribute = function() {

    var localctx = new HtmlAttributeContext(this, this._ctx, this.state);
    this.enterRule(localctx, 10, HTMLParser.RULE_htmlAttribute);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 108;
        this.match(HTMLParser.TAG_NAME);
        this.state = 111;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===HTMLParser.TAG_EQUALS) {
            this.state = 109;
            this.match(HTMLParser.TAG_EQUALS);
            this.state = 110;
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

HtmlChardataContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlChardata(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlChardataContext = HtmlChardataContext;

HTMLParser.prototype.htmlChardata = function() {

    var localctx = new HtmlChardataContext(this, this._ctx, this.state);
    this.enterRule(localctx, 12, HTMLParser.RULE_htmlChardata);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 113;
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

HtmlMiscContext.prototype.SEA_WS = function() {
    return this.getToken(HTMLParser.SEA_WS, 0);
};

HtmlMiscContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlMisc(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlMiscContext = HtmlMiscContext;

HTMLParser.prototype.htmlMisc = function() {

    var localctx = new HtmlMiscContext(this, this._ctx, this.state);
    this.enterRule(localctx, 14, HTMLParser.RULE_htmlMisc);
    try {
        this.state = 117;
        this._errHandler.sync(this);
        switch(this._input.LA(1)) {
        case HTMLParser.HTML_COMMENT:
        case HTMLParser.HTML_CONDITIONAL_COMMENT:
            this.enterOuterAlt(localctx, 1);
            this.state = 115;
            this.htmlComment();
            break;
        case HTMLParser.SEA_WS:
            this.enterOuterAlt(localctx, 2);
            this.state = 116;
            this.match(HTMLParser.SEA_WS);
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

HtmlCommentContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitHtmlComment(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.HtmlCommentContext = HtmlCommentContext;

HTMLParser.prototype.htmlComment = function() {

    var localctx = new HtmlCommentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 16, HTMLParser.RULE_htmlComment);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 119;
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

ScriptContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitScript(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.ScriptContext = ScriptContext;

HTMLParser.prototype.script = function() {

    var localctx = new ScriptContext(this, this._ctx, this.state);
    this.enterRule(localctx, 18, HTMLParser.RULE_script);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 121;
        this.match(HTMLParser.SCRIPT_OPEN);
        this.state = 122;
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

StyleContext.prototype.accept = function(visitor) {
    if ( visitor instanceof HTMLParserVisitor ) {
        return visitor.visitStyle(this);
    } else {
        return visitor.visitChildren(this);
    }
};




HTMLParser.StyleContext = StyleContext;

HTMLParser.prototype.style = function() {

    var localctx = new StyleContext(this, this._ctx, this.state);
    this.enterRule(localctx, 20, HTMLParser.RULE_style);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 124;
        this.match(HTMLParser.STYLE_OPEN);
        this.state = 125;
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
