// Generated from /Users/sansarip/Dev/owlbear/antlr4/HTMLParser.g4 by ANTLR 4.8
// jshint ignore: start
var antlr4 = require('antlr4/index');

// This class defines a complete generic visitor for a parse tree produced by HTMLParser.

function HTMLParserVisitor() {
	antlr4.tree.ParseTreeVisitor.call(this);
	return this;
}

HTMLParserVisitor.prototype = Object.create(antlr4.tree.ParseTreeVisitor.prototype);
HTMLParserVisitor.prototype.constructor = HTMLParserVisitor;

// Visit a parse tree produced by HTMLParser#htmlDocument.
HTMLParserVisitor.prototype.visitHtmlDocument = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#scriptletOrSeaWs.
HTMLParserVisitor.prototype.visitScriptletOrSeaWs = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlElements.
HTMLParserVisitor.prototype.visitHtmlElements = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlElement.
HTMLParserVisitor.prototype.visitHtmlElement = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlContent.
HTMLParserVisitor.prototype.visitHtmlContent = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlAttribute.
HTMLParserVisitor.prototype.visitHtmlAttribute = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlChardata.
HTMLParserVisitor.prototype.visitHtmlChardata = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlMisc.
HTMLParserVisitor.prototype.visitHtmlMisc = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#htmlComment.
HTMLParserVisitor.prototype.visitHtmlComment = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#script.
HTMLParserVisitor.prototype.visitScript = function(ctx) {
  return this.visitChildren(ctx);
};


// Visit a parse tree produced by HTMLParser#style.
HTMLParserVisitor.prototype.visitStyle = function(ctx) {
  return this.visitChildren(ctx);
};



exports.HTMLParserVisitor = HTMLParserVisitor;