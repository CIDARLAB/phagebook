!function(a,b,c){"use strict";b.module("ngCookies",["ng"]).factory("$cookies",["$rootScope","$browser",function(a,d){function e(){var a,e,f,i;for(a in h)k(g[a])&&d.cookies(a,c);for(a in g)e=g[a],b.isString(e)||(e=""+e,g[a]=e),e!==h[a]&&(d.cookies(a,e),i=!0);if(i){i=!1,f=d.cookies();for(a in g)g[a]!==f[a]&&(k(f[a])?delete g[a]:g[a]=f[a],i=!0)}}var f,g={},h={},i=!1,j=b.copy,k=b.isUndefined;return d.addPollFn(function(){var b=d.cookies();f!=b&&(f=b,j(b,h),j(b,g),i&&a.$apply())})(),i=!0,a.$watch(e),g}]).factory("$cookieStore",["$cookies",function(a){return{get:function(c){var d=a[c];return d?b.fromJson(d):d},put:function(c,d){a[c]=b.toJson(d)},remove:function(b){delete a[b]}}}])}(window,window.angular),function(a,b){"use strict";function c(){this.$get=["$$sanitizeUri",function(a){return function(b){var c=[];return f(b,i(c,function(b,c){return!/^unsafe/.test(a(b,c))})),c.join("")}}]}function d(a){var c=[],d=i(c,b.noop);return d.chars(a),c.join("")}function e(a){var b,c={},d=a.split(",");for(b=0;b<d.length;b++)c[d[b]]=!0;return c}function f(a,c){function d(a,d,f,h){if(d=b.lowercase(d),y[d])for(;s.last()&&z[s.last()];)e("",s.last());x[d]&&s.last()==d&&e("",d),h=u[d]||!!h,h||s.push(d);var i={};f.replace(m,function(a,b,c,d,e){var f=c||d||e||"";i[b]=g(f)}),c.start&&c.start(d,i,h)}function e(a,d){var e,f=0;if(d=b.lowercase(d))for(f=s.length-1;f>=0&&s[f]!=d;f--);if(f>=0){for(e=s.length-1;e>=f;e--)c.end&&c.end(s[e]);s.length=f}}var f,h,i,s=[],t=a;for(s.last=function(){return s[s.length-1]};a;){if(h=!0,s.last()&&A[s.last()])a=a.replace(new RegExp("(.*)<\\s*\\/\\s*"+s.last()+"[^>]*>","i"),function(a,b){return b=b.replace(p,"$1").replace(r,"$1"),c.chars&&c.chars(g(b)),""}),e("",s.last());else if(0===a.indexOf("<!--")?(f=a.indexOf("--",4),f>=0&&a.lastIndexOf("-->",f)===f&&(c.comment&&c.comment(a.substring(4,f)),a=a.substring(f+3),h=!1)):q.test(a)?(i=a.match(q),i&&(a=a.replace(i[0],""),h=!1)):o.test(a)?(i=a.match(l),i&&(a=a.substring(i[0].length),i[0].replace(l,e),h=!1)):n.test(a)&&(i=a.match(k),i&&(a=a.substring(i[0].length),i[0].replace(k,d),h=!1)),h){f=a.indexOf("<");var v=0>f?a:a.substring(0,f);a=0>f?"":a.substring(f),c.chars&&c.chars(g(v))}if(a==t)throw j("badparse","The sanitizer was unable to parse the following block of html: {0}",a);t=a}e()}function g(a){if(!a)return"";var b=F.exec(a),c=b[1],d=b[3],e=b[2];return e&&(E.innerHTML=e.replace(/</g,"&lt;"),e="textContent"in E?E.textContent:E.innerText),c+e+d}function h(a){return a.replace(/&/g,"&amp;").replace(s,function(a){var b=a.charCodeAt(0),c=a.charCodeAt(1);return"&#"+(1024*(b-55296)+(c-56320)+65536)+";"}).replace(t,function(a){return"&#"+a.charCodeAt(0)+";"}).replace(/</g,"&lt;").replace(/>/g,"&gt;")}function i(a,c){var d=!1,e=b.bind(a,a.push);return{start:function(a,f,g){a=b.lowercase(a),!d&&A[a]&&(d=a),d||B[a]!==!0||(e("<"),e(a),b.forEach(f,function(d,f){var g=b.lowercase(f),i="img"===a&&"src"===g||"background"===g;D[g]!==!0||C[g]===!0&&!c(d,i)||(e(" "),e(f),e('="'),e(h(d)),e('"'))}),e(g?"/>":">"))},end:function(a){a=b.lowercase(a),d||B[a]!==!0||(e("</"),e(a),e(">")),a==d&&(d=!1)},chars:function(a){d||e(h(a))}}}var j=b.$$minErr("$sanitize"),k=/^<\s*([\w:-]+)((?:\s+[\w:-]+(?:\s*=\s*(?:(?:"[^"]*")|(?:'[^']*')|[^>\s]+))?)*)\s*(\/?)\s*>/,l=/^<\s*\/\s*([\w:-]+)[^>]*>/,m=/([\w:-]+)(?:\s*=\s*(?:(?:"((?:[^"])*)")|(?:'((?:[^'])*)')|([^>\s]+)))?/g,n=/^</,o=/^<\s*\//,p=/<!--(.*?)-->/g,q=/<!DOCTYPE([^>]*?)>/i,r=/<!\[CDATA\[(.*?)]]>/g,s=/[\uD800-\uDBFF][\uDC00-\uDFFF]/g,t=/([^\#-~| |!])/g,u=e("area,br,col,hr,img,wbr"),v=e("colgroup,dd,dt,li,p,tbody,td,tfoot,th,thead,tr"),w=e("rp,rt"),x=b.extend({},w,v),y=b.extend({},v,e("address,article,aside,blockquote,caption,center,del,dir,div,dl,figure,figcaption,footer,h1,h2,h3,h4,h5,h6,header,hgroup,hr,ins,map,menu,nav,ol,pre,script,section,table,ul")),z=b.extend({},w,e("a,abbr,acronym,b,bdi,bdo,big,br,cite,code,del,dfn,em,font,i,img,ins,kbd,label,map,mark,q,ruby,rp,rt,s,samp,small,span,strike,strong,sub,sup,time,tt,u,var")),A=e("script,style"),B=b.extend({},u,y,z,x),C=e("background,cite,href,longdesc,src,usemap"),D=b.extend({},C,e("abbr,align,alt,axis,bgcolor,border,cellpadding,cellspacing,class,clear,color,cols,colspan,compact,coords,dir,face,headers,height,hreflang,hspace,ismap,lang,language,nohref,nowrap,rel,rev,rows,rowspan,rules,scope,scrolling,shape,size,span,start,summary,target,title,type,valign,value,vspace,width")),E=document.createElement("pre"),F=/^(\s*)([\s\S]*?)(\s*)$/;b.module("ngSanitize",[]).provider("$sanitize",c),b.module("ngSanitize").filter("linky",["$sanitize",function(a){var c=/((ftp|https?):\/\/|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s.;,(){}<>]/,e=/^mailto:/;return function(f,g){function h(a){a&&n.push(d(a))}function i(a,c){n.push("<a "),b.isDefined(g)&&(n.push('target="'),n.push(g),n.push('" ')),n.push('href="'),n.push(a),n.push('">'),h(c),n.push("</a>")}if(!f)return f;for(var j,k,l,m=f,n=[];j=m.match(c);)k=j[0],j[2]==j[3]&&(k="mailto:"+k),l=j.index,h(m.substr(0,l)),i(k,j[0].replace(e,"")),m=m.substring(l+j[0].length);return h(m),a(n.join(""))}}])}(window,window.angular),function(a,b){"use strict";function c(){function a(a,c){return b.extend(new(b.extend(function(){},{prototype:a})),c)}function c(a,b){var c=b.caseInsensitiveMatch,d={originalPath:a,regexp:a},e=d.keys=[];return a=a.replace(/([().])/g,"\\$1").replace(/(\/)?:(\w+)([\?\*])?/g,function(a,b,c,d){var f="?"===d?d:null,g="*"===d?d:null;return e.push({name:c,optional:!!f}),b=b||"",""+(f?"":b)+"(?:"+(f?b:"")+(g&&"(.+?)"||"([^/]+)")+(f||"")+")"+(f||"")}).replace(/([\/$\*])/g,"\\$1"),d.regexp=new RegExp("^"+a+"$",c?"i":""),d}var d={};this.when=function(a,e){if(d[a]=b.extend({reloadOnSearch:!0},e,a&&c(a,e)),a){var f="/"==a[a.length-1]?a.substr(0,a.length-1):a+"/";d[f]=b.extend({redirectTo:a},c(f,e))}return this},this.otherwise=function(a){return this.when(null,a),this},this.$get=["$rootScope","$location","$routeParams","$q","$injector","$http","$templateCache","$sce",function(c,e,f,g,h,i,j,k){function l(a,b){var c=b.keys,d={};if(!b.regexp)return null;var e=b.regexp.exec(a);if(!e)return null;for(var f=1,g=e.length;g>f;++f){var h=c[f-1],i="string"==typeof e[f]?decodeURIComponent(e[f]):e[f];h&&i&&(d[h.name]=i)}return d}function m(){var a=n(),d=q.current;a&&d&&a.$$route===d.$$route&&b.equals(a.pathParams,d.pathParams)&&!a.reloadOnSearch&&!p?(d.params=a.params,b.copy(d.params,f),c.$broadcast("$routeUpdate",d)):(a||d)&&(p=!1,c.$broadcast("$routeChangeStart",a,d),q.current=a,a&&a.redirectTo&&(b.isString(a.redirectTo)?e.path(o(a.redirectTo,a.params)).search(a.params).replace():e.url(a.redirectTo(a.pathParams,e.path(),e.search())).replace()),g.when(a).then(function(){if(a){var c,d,e=b.extend({},a.resolve);return b.forEach(e,function(a,c){e[c]=b.isString(a)?h.get(a):h.invoke(a,null,null,c)}),b.isDefined(c=a.template)?b.isFunction(c)&&(c=c(a.params)):b.isDefined(d=a.templateUrl)&&(b.isFunction(d)&&(d=d(a.params)),d=k.getTrustedResourceUrl(d),b.isDefined(d)&&(a.loadedTemplateUrl=d,c=i.get(d,{cache:j}).then(function(a){return a.data}))),b.isDefined(c)&&(e.$template=c),g.all(e)}}).then(function(e){a==q.current&&(a&&(a.locals=e,b.copy(a.params,f)),c.$broadcast("$routeChangeSuccess",a,d))},function(b){a==q.current&&c.$broadcast("$routeChangeError",a,d,b)}))}function n(){var c,f;return b.forEach(d,function(d){!f&&(c=l(e.path(),d))&&(f=a(d,{params:b.extend({},e.search(),c),pathParams:c}),f.$$route=d)}),f||d[null]&&a(d[null],{params:{},pathParams:{}})}function o(a,c){var d=[];return b.forEach((a||"").split(":"),function(a,b){if(0===b)d.push(a);else{var e=a.match(/(\w+)(.*)/),f=e[1];d.push(c[f]),d.push(e[2]||""),delete c[f]}}),d.join("")}var p=!1,q={routes:d,reload:function(){p=!0,c.$evalAsync(m)}};return c.$on("$locationChangeSuccess",m),q}]}function d(){this.$get=function(){return{}}}function e(a,c,d){return{restrict:"ECA",terminal:!0,priority:400,transclude:"element",link:function(e,f,g,h,i){function j(){n&&(n.remove(),n=null),l&&(l.$destroy(),l=null),m&&(d.leave(m,function(){n=null}),n=m,m=null)}function k(){var g=a.current&&a.current.locals,h=g&&g.$template;if(b.isDefined(h)){var k=e.$new(),n=a.current,q=i(k,function(a){d.enter(a,null,m||f,function(){!b.isDefined(o)||o&&!e.$eval(o)||c()}),j()});m=q,l=n.scope=k,l.$emit("$viewContentLoaded"),l.$eval(p)}else j()}var l,m,n,o=g.autoscroll,p=g.onload||"";e.$on("$routeChangeSuccess",k),k()}}}function f(a,b,c){return{restrict:"ECA",priority:-400,link:function(d,e){var f=c.current,g=f.locals;e.html(g.$template);var h=a(e.contents());if(f.controller){g.$scope=d;var i=b(f.controller,g);f.controllerAs&&(d[f.controllerAs]=i),e.data("$ngControllerController",i),e.children().data("$ngControllerController",i)}h(d)}}}var g=b.module("ngRoute",["ng"]).provider("$route",c);g.provider("$routeParams",d),g.directive("ngView",e),g.directive("ngView",f),e.$inject=["$route","$anchorScroll","$animate"],f.$inject=["$compile","$controller","$route"]}(window,window.angular);