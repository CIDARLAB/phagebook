angular.module("clotho.tokenizer", []), angular.module("clotho.commandbar", ["clotho.foundation", "clotho.tokenizer", "ui.keypress"]), angular.module("ui.keypress", []).factory("keypressHelper", ["$parse",
    function(a) {
        var b = {
            8: "backspace",
            9: "tab",
            13: "enter",
            27: "esc",
            32: "space",
            33: "pageup",
            34: "pagedown",
            35: "end",
            36: "home",
            37: "left",
            38: "up",
            39: "right",
            40: "down",
            45: "insert",
            46: "delete"
        }, c = function(a) {
            return a.charAt(0).toUpperCase() + a.slice(1)
        };
        return function(d, e, f, g) {
            var h, i = [];
            h = e.$eval(g["ui" + c(d)]), angular.forEach(h, function(b, c) {
                var d, e;
                e = a(b), angular.forEach(c.split(" "), function(a) {
                    d = {
                        expression: e,
                        keys: {}
                    }, angular.forEach(a.split("-"), function(a) {
                        d.keys[a] = !0
                    }), i.push(d)
                })
            }), f.bind(d, function(a) {
                var c = !(!a.metaKey || a.ctrlKey),
                    f = !! a.altKey,
                    g = !! a.ctrlKey,
                    h = !! a.shiftKey,
                    j = a.keyCode;
                "keypress" === d && !h && j >= 97 && 122 >= j && (j -= 32), angular.forEach(i, function(d) {
                    var i = d.keys[b[j]] || d.keys[j.toString()],
                        k = !! d.keys.meta,
                        l = !! d.keys.alt,
                        m = !! d.keys.ctrl,
                        n = !! d.keys.shift;
                    i && k === c && l === f && m === g && n === h && e.$apply(function() {
                        d.expression(e, {
                            $event: a
                        })
                    })
                })
            })
        }
    }
]),
    angular.module("ui.keypress").directive("uiKeydown", ["keypressHelper",
    function(a) {
        return {
            link: function(b, c, d) {
                a("keydown", b, c, d)
            }
        }
    }
]), angular.module("ui.keypress").directive("uiKeypress", ["keypressHelper",
    function(a) {
        return {
            link: function(b, c, d) {
                a("keypress", b, c, d)
            }
        }
    }
]), angular.module("ui.keypress").directive("uiKeyup", ["keypressHelper",
    function(a) {
        return {
            link: function(b, c, d) {
                a("keyup", b, c, d)
            }
        }
    }
]), angular.module("clotho.tokenizer").directive("autoGrow", ["$timeout",
    function(a) {
        return {
            link: function(b, c) {
                var d = (c.css("paddingLeft"), c.css("paddingRight"), 100),
                    e = angular.element("<span></span>").css({
                        position: "absolute",
                        top: "-10000px",
                        left: "-10000px",
                        fontSize: c.css("fontSize"),
                        fontFamily: c.css("fontFamily"),
                        "white-space": "pre"
                    });
                c.after(e);
                var f = function() {
                    var a = c.val().replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/&/g, "&amp;");
                    e.html("" !== a ? a : c[0].placeholder);
                    var b = e[0].offsetWidth + 26,
                        f = Math.max(b, d) + "px";
                    c.css("width", f)
                };
                c.bind("keyup keydown blur", f), a(f)
            }
        }
    }
]), angular.module("clotho.commandbar").service("CommandBar", ["Clotho", "ClientAPI", "ClothoCommandHistory", "Debug", "ClothoSchemas", "$timeout", "$q", "$document",
    function(a, b, c, d, e, f, g, h) {
        var i = {
                dateFilter: "mediumTime",
                timeFilter: "timestamp"
            }, j = (new d("Command Bar", "#ffbb55"), function() {
                return angular.element(h[0].querySelector("[clotho-command-bar]"))
            }),
            k = function() {
                return angular.element(h[0].querySelector("[clotho-command-bar] [clotho-tokenizer]"))
            }, l = function() {
                return angular.element(h[0].querySelector("[clotho-command-bar] [clotho-autocomplete]"))
            }, m = "query",
            n = function() {
                l().focus()
            }, o = function(a) {
                l().scope().setQueryString(a)
            }, p = {};
        p.query = "", p.log = !1, p.logSnippet = !1, p.toggle = function(a, b) {
            p[a] = angular.isDefined(b) ? b : !p[a]
        }, p.toggleActivityLog = function() {
            p.log = !p.log, p.log && (log.unread = "")
        };
        var q = function(c) {
            if ((angular.isEmpty(c) || !angular.isObject(c)) && (c = p.query), c.query = angular.isDefined(c.query) ? c.query.trim() : "", c.query) {
                var d = {
                    "class": "info",
                    from: "client",
                    text: c.query,
                    timestamp: Date.now()
                };
                return b.say(d), a.submit(c).then(function(a) {
                    p.query = "", b.say({
                        text: a,
                        "class": "success"
                    })
                }, function() {})
            }
            return g.when(!1)
        };
        return {
            options: i,
            display: p,
            setQuery: function(a, b) {
                angular.isDefined(b) && b.preventDefault(), a && (p.query = angular.isEmpty(a.value) ? a.text : a.value)
            },
            submit: q,
            getCommandBarElement: j,
            getTokenizerElement: k,
            getCommandBarInput: l,
            commandBarInputModel: m,
            focusInput: n,
            setInput: o
        }
    }
]), angular.module("clotho.commandbar").directive("clothoCommandBar", ["Clotho", "CommandBar", "ClothoCommandHistory", "terminalAsideOptions", "$location", "$window", "$compile", "$timeout", "$clothoModal",
    function(a, b, c, d, e, f, g, h, i) {
        return {
            restrict: "A",
            replace: !0,
            scope: {},
            templateUrl: "html/commandBar.html",
            controller: ["$scope", "$element", "$attrs",
                function(a) {
                    a.options = b.options, a.log = b.log, a.logEntries = c.entries, a.autocomplete = b.autocomplete, a.display = b.display, a.setQuery = b.setQuery, a.submit = b.submit, a.execute = b.execute;
                    var d = !1;
                    a.toggleLogin = function(a) {
                        d = angular.isDefined(a) ? a : !d, d ? i.create({
                            title: "Clotho Login",
                            "template-url": "'html/simpleLogin.html'"
                        }) : i.destroy()
                    };
                    var e = null,
                        f = 1e4;
                    a.startLogTimeout = function() {
                        a.cancelLogTimeout(), e = h(function() {
                            a.display.toggle("logSnippet", !1)
                        }, f)
                    }, a.cancelLogTimeout = function() {
                        h.cancel(e)
                    }, a.$watchCollection("logEntries", function() {
                        a.display.toggle("logSnippet", !0), a.startLogTimeout()
                    })
                }
            ],
            link: function(b) {
                var rootURL = e.protocol() + "://" + e.host() + ":" + e.port();
                b.toggleTerminalAside = c.toggleTerminal, b.showMeHow = function() {
                    a.query({
                        name: "Learning Clotho"
                    }).then(function(b) {
                        a.startTrail(b[0].id)
                    })
                }, b.goProfile = function() {
                    f.location.href = rootURL + "/html/profile.html";
                }, b.aboutClotho = function() {
                    f.open('https://www.clothocad.org/','_blank');
                }, b.teamPhagebook = function() {
                    f.location.href = rootURL + "/html/team.html";
                }, b.goProject = function() {
                    f.location.href = rootURL + "/html/newProject.html";
                }, b.goOrder = function() {
                    f.location.href = rootURL + "/html/ordering.html";
                }, b.logout = function() {
                    Clotho.logout();
                    sessionStorage.clear();
                    f.location.href = rootURL;
                }, b.aboutPhagebook = function() {
                    f.location.href = rootURL + "/html/about.html";
                }
            }
        }
    }
]), angular.module("clotho.commandbar").controller("TerminalCtrl", ["$scope", "ClothoCommandHistory",
    function(a, b) {
        a.logEntries = b.entries
    }
]), angular.module("clotho.commandbar").value("terminalAsideOptions", {
    visible: !1
}).directive("terminalAside", ["$http", "$q", "$templateCache", "$window", "$animate", "$compile", "terminalAsideOptions", "ClothoCommandHistory", "Clotho",
    function(a, b, c, d, e, f, g, h, i) {
        return {
            restrict: "EA",
            templateUrl: "html/commandViews/terminalAside.html",
            replace: !0,
            scope: {
                title: "=?asideTitle",
                contentUrl: "=?asideContentUrl"
            },
            controller: ["$scope", "$element", "$attrs",
                function(a) {
                    a.submit = function() {
                        a.terminalQuery && i.submit(a.terminalQuery)
                    }
                }
            ],
            link: function(d, e) {
                function j(d) {
                    return b.when(c.get(d) || a.get(d)).then(function(a) {
                        return angular.isObject(a) ? (c.put(d, a.data), a.data) : a
                    })
                }
                d.$hide = function() {
                    g.visible = !1
                }, d.$show = function() {
                    g.visible = !0
                }, d.$toggle = function(a) {
                    g.visible = angular.isDefined(a) ? a : !g.visible, angular.element("body").attr("aside-status", g.visible ? "active" : "")
                }, d.$watch("contentUrl", function(a) {
                    a && j(a).then(function(a) {
                        console.info("ASIDE TEMPLATE" + a), d.content = f(a)(d)
                    })
                }), i.listen("toggleTerminalActive", d.$toggle, d), d.$watch(function() {
                    return g.visible
                }, function(a) {
                    a ? (e.addClass("active"), h.setLastView()) : e.removeClass("active")
                })
            }
        }
    }
]).directive("terminalAsideTrigger", ["Clotho", "$timeout", "terminalAsideOptions",
    function(a, b, c) {
        return {
            restrict: "A",
            template: '<div id="terminalAsideTrigger" ng-click="toggle()" ng-attr-status="{{activeClass ? \'active\' : \'\'}}" ng-class="{active : activeClass}"></div>',
            replace: !0,
            scope: !0,
            link: function(d) {
                d.toggle = function() {
                    a.trigger("toggleTerminalActive"), b(function() {
                        d.activeClass = c.visible, angular.element("body").attr("aside-status", c.visible ? "active" : "")
                    })
                }
            }
        }
    }
]), angular.module("clotho.commandbar").directive("logEntries", function() {
    return {
        restrict: "A",
        templateUrl: "html/commandViews/logEntries.html",
        scope: {
            entries: "=logEntries"
        }
    }
}), angular.module("clotho.commandbar").controller("loginCtrl", ["$scope", "$timeout", "Clotho",
    function(a, b, c) {
        a.notification = {}, a.cred = {
            username: "",
            password: ""
        }, a.login = function() {
            c.login(a.cred.username, a.cred.password).then(function(b) {
                console.log("run login", b), b ? a.notification = {
                    "class": "alert-success",
                    message: "Log in Success"
                } : (a.notification = {
                    "class": "alert-danger",
                    message: "Log in Error"
                }, a.cred = {
                    username: "",
                    password: ""
                })
            })
        }
    }
]), angular.module("clotho.commandbar").directive("clothoFunctionExecutor", ["$filter", "Clotho", "ClothoSchemas",
    function(a, b, c) {
        return {
            scope: {
                "function": "=",
                onExecute: "&?"
            },
            templateUrl: "html/commandViews/executor.html",
            link: function(d) {
                function e() {
                    d.functionArgs = {}
                }

                function f(a, b) {
                    var c = [];
                    return angular.forEach(a.args, function(a) {
                        c.push(b[a.name])
                    }), c
                }
                d.isPrimitiveField = c.isPrimitiveField, d.schemaReadable = c.mapSchemaIdToName, d.capitalize = function(a) {
                    return a.substring(0, 1).toUpperCase() + a.substr(1)
                }, d.queryWrapper = function(d, e) {
                    return b.autocomplete(d).then(function(b) {
                        return angular.isUndefined(e) ? b : a("filter")(b, function(a) {
                            return "function" == e ? c.isFunction(a) : c.isInstanceOfSchema(a, e)
                        })
                    })
                }, d.executeFunction = function() {
                    b.run(d.function.id, f(d.function, d.functionArgs)).then(function(a) {
                        d.functionResult = a, d.onExecute({
                            $result: a
                        })
                    })
                }, d.clearArguments = function() {
                    e(), d.functionResult = null
                }, d.setArgument = function(a, c) {
                    b.get(c, {
                        mute: !0
                    }).then(function(b) {
                        d.functionArgs[a] = b
                    })
                }, d.$watch("function.id", function() {
                    e()
                })
            }
        }
    }
]), angular.module("clotho.tokenizer").directive("clothoAutocomplete", ["Clotho", "$q", "$parse", "$timeout", "$compile", "$filter", "$document",
    function(a, b, c, d, e, f, g) {
        var h = [8, 9, 13, 27, 37, 38, 39, 40],
            i = 32,
            j = i,
            k = " ",
            l = 0;
        return {
            restrict: "A",
            require: "?ngModel",
            link: function(b, i, m) {
                function n() {
                    b.query = ""
                }

                function o() {
                    b.autocompletions = [], b.activeIdx = -1
                }

                function p() {
                    n(), o(), b.hasFocus = !1, angular.isDefined(b.tokenCollection) && b.tokenCollection.unsetActive(), b.$digest()
                }

                function q(a) {
                    return s.test(a.charAt(0))
                }

                function r(a) {
                    b.hasFocus && (angular.isUndefined(b.tokenCollection) || !b.tokenCollection.isActive()) && b.activeIdx < 0 && (angular.isUndefined(a) || !i[0].contains(a.target)) && d(p)
                }
                i.attr({
                    autocomplete: "off",
                    autocorrect: "off",
                    autocapitalize: "off",
                    spellcheck: "false"
                });
                var s = /^['"].*/,
                    t = c(m.autocompleteOnSelect),
                    u = angular.element("<clotho-autocomplete-listing></clotho-autocomplete-listing>");
                u.attr({
                    autocompletions: "autocompletions",
                    active: "activeIdx",
                    select: "select(activeIdx)",
                    "has-focus": "hasFocus",
                    query: "query"
                }), b.hasFocus = !1;
                var v = function(c) {
                    s.test(c) && (c = c.substring(1)), 0 !== c.length && a.autocomplete(c).then(function(a) {
                        a && a.length && b.query.length ? b.autocompletions = f("limitTo")(a, 10) : o()
                    })
                };
                b.query = "";
                var w;
                b.$watch("query", function(a) {
                    a && a.length ? (b.hasFocus = !0, angular.isDefined(b.tokenCollection) && b.tokenCollection.unsetActive(), l > 0 ? (w && d.cancel(w), w = d(function() {
                        v(a)
                    }, l)) : v(a)) : o()
                }), b.setQueryString = function(a, c) {
                    a = angular.isUndefined(a) || angular.isEmpty(a) ? b.query : a, n(), o(), c || angular.isDefined(b.tokenCollection) && b.tokenCollection.removeAll(), angular.forEach(a.split(k), function(a) {
                        a.length && b.addToken(a)
                    })
                }, b.select = function(a) {
                    console.log("selecting ", a);
                    var c = a > -1 ? b.autocompletions[a] : b.query;
                    c && t(b, {
                        $item: c,
                        $query: b.query
                    }), n(), d(function() {
                        o(), i[0].focus()
                    }, 0, !1)
                }, i.bind("keydown", function(a) {
                    if (a.which === j && ("" == b.query ? a.preventDefault() : q(b.query) || (b.$apply(function() {
                            b.select(1 == b.autocompletions.length ? 0 : -1)
                        }), a.preventDefault())), -1 !== h.indexOf(a.which)) {
                        if (8 === a.which) {
                            if (b.query.length) return;
                            if (angular.isDefined(b.tokenCollection))
                                if (b.tokenCollection.isActive()) {
                                    var c = b.tokenCollection.whichActive();
                                    b.tokenCollection.removeActiveToken(), b.tokenCollection.setActive(c)
                                } else b.tokenCollection.setLastActive();
                            b.$digest()
                        } else if (40 === a.which) b.autocompletions.length && (b.activeIdx = (b.activeIdx + 1) % b.autocompletions.length, b.$digest());
                        else if (38 === a.which) b.autocompletions.length && (b.activeIdx = (b.activeIdx ? b.activeIdx : b.autocompletions.length) - 1, b.$digest());
                        else if (37 === a.which) {
                            if (!angular.isDefined(b.tokenCollection) || !b.tokenCollection.isActive()) return;
                            b.tokenCollection.setPrevActive(), b.$digest()
                        } else if (39 === a.which) {
                            if (!angular.isDefined(b.tokenCollection) || !b.tokenCollection.isActive()) return;
                            b.tokenCollection.isLastActive() ? b.tokenCollection.unsetActive() : b.tokenCollection.setNextActive(), b.$digest()
                        } else 13 === a.which || 9 === a.which ? (console.log("hit enter", b.activeIdx), b.activeIdx >= 0 ? b.$apply(function() {
                            b.select(b.activeIdx)
                        }) : (b.query.length && b.$apply(function() {
                            b.select()
                        }), b.$apply(function() {
                            b.submit()
                        })), b.$digest()) : 27 === a.which && (b.query.length && b.autocompletions.length ? (o(), angular.isDefined(b.tokenCollection) && b.tokenCollection.unsetActive(), b.$digest()) : (i.blur(), p()));
                        a.preventDefault()
                    }
                }), i.on("focus", function() {
                    d(function() {
                        b.hasFocus = !0
                    })
                }), i.on("paste", function() {
                    d(function() {
                        b.setQueryString(null, !0)
                    })
                }), b.$on("$locationChangeSuccess", function() {
                    setTimeout(p)
                }), g.bind("click", r), b.$on("$destroy", function() {
                    g.unbind("click", r)
                }), o(), i.after(e(u)(b))
            }
        }
    }
]), angular.module("clotho.tokenizer").directive("clothoAutocompleteListing", function() {
    return {
        restrict: "EA",
        scope: {
            autocompletions: "=",
            query: "=",
            active: "=",
            hasFocus: "=",
            select: "&"
        },
        replace: !0,
        templateUrl: "html/commandViews/autocompleteListing.html",
        link: function(a) {
            a.isOpen = function() {
                return a.hasFocus && a.matches.length > 0
            }, a.isActive = function(b) {
                return a.active == b
            }, a.selectActive = function(b) {
                a.active = b
            }, a.selectMatch = function(b) {
                a.select({
                    activeIdx: b
                })
            }
        }
    }
}), angular.module("clotho.tokenizer").directive("clothoAutocompleteMatch", ["ClothoSchemas",
    function(a) {
        return {
            restrict: "EA",
            replace: !0,
            scope: {
                index: "=",
                match: "=",
                query: "="
            },
            templateUrl: "html/commandViews/autocompleteMatch.html",
            link: function(b, c, d) {
                b.$watch(function() {
                    return d.active
                }, function(a) {
                    b.active = b.$eval(a)
                }), b.$watch("match", function(c) {
                    b.iconClass = a.determineSharableIcon(a.dirtyDetermineType(c))
                })
            }
        }
    }
]).filter("clothoAutocompleteHighlight", function() {
    function a(a) {
        return a.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1")
    }
    return function(b, c) {
        return c ? b.replace(new RegExp(a(c), "gi"), "<strong>$&</strong>") : b
    }
}), angular.module("clotho.tokenizer").directive("clothoToken", ["$parse", "Clotho", "clothoTokenFactory",
    function(a, b, c) {
        return {
            restrict: "E",
            templateUrl: "html/commandViews/token.html",
            scope: {
                token: "=?",
                tokenModel: "=?",
                tokenName: "@?",
                tokenActivePass: "@?",
                popupPlacement: "@?",
                popupTrigger: "@?",
                onClick: "&?",
                onRemove: "&?"
            },
            link: function(a, b) {
                a.$watch("tokenActivePass", function(b) {
                    a.tokenActive = a.$eval(b)
                }), a.$watch("tokenModel", function(b) {
                    angular.isEmpty(b) || (a.token = new c(b))
                }), b.on("click", function(b) {
                    a.$apply(function() {
                        a.tokenActive = !a.tokenActive
                    }), a.onClick({
                        $event: b,
                        $token: a.token
                    })
                }), a.$on("$destroy", function() {
                    a.tokenActive = !1, a.onRemove({
                        $token: a.token
                    })
                })
            }
        }
    }
]), angular.module("clotho.tokenizer").factory("clothoTokenCollectionFactory", ["clothoTokenFactory",
    function(a) {
        function b(a) {
            var b = this;
            this.tokens = [], this.currentTokenIndex = -1, (angular.isArray(a) || angular.isObject(a)) && angular.forEach(a, function(a) {
                b.addToken(a)
            })
        }
        return b.prototype.addToken = function(b) {
            this.tokens.push(new a(b))
        }, b.prototype.inRange = function(a) {
            return a > -1 && a < this.tokens.length
        }, b.prototype.getToken = function(a) {
            return this.tokens[a]
        }, b.prototype.indexOf = function(a) {
            return this.tokens.indexOf(a)
        }, b.prototype.removeToken = function(a) {
            return this.inRange(a) ? this.tokens.splice(a, 1) : !1
        }, b.prototype.removeAll = function() {
            this.tokens.length = 0
        }, b.prototype.removeActiveToken = function() {
            if (this.isActive()) {
                var a = this.removeToken(this.currentTokenIndex);
                return this.unsetActive(), a
            }
            return !1
        }, b.prototype.setActive = function(a) {
            return this.inRange(a) ? (this.currentTokenIndex = a, a) : !1
        }, b.prototype.toggleActive = function(a) {
            this[this.isActive(a) ? "unsetActive" : "setActive"](a)
        }, b.prototype.setLastActive = function() {
            this.setActive(this.tokens.length - 1)
        }, b.prototype.setPrevActive = function() {
            this.currentTokenIndex = (this.currentTokenIndex > 0 ? this.currentTokenIndex : this.tokens.length) - 1
        }, b.prototype.setNextActive = function() {
            this.currentTokenIndex = (this.currentTokenIndex + 1) % this.tokens.length
        }, b.prototype.unsetActive = function() {
            this.currentTokenIndex = -1
        }, b.prototype.isActive = function(a) {
            return angular.isDefined(a) ? this.currentTokenIndex == a : this.currentTokenIndex > -1
        }, b.prototype.whichActive = function() {
            return this.currentTokenIndex
        }, b.prototype.isLastActive = function() {
            return this.currentTokenIndex === this.tokens.length - 1
        }, b
    }
]), angular.module("clotho.tokenizer").factory("clothoTokenFactory", ["Clotho",
    function(a) {
        function b(b) {
            var c = this;
            c.model = b, this.isSharable() && (c.fullSharablePromise = a.get(c.model.id, {
                mute: !0
            }).then(function(a) {
                c.fullSharable = a
            }))
        }
        return b.prototype.readable = function() {
            return this.model.name || this.model
        }, b.prototype.id = function() {
            return this.model.id || null
        }, b.prototype.isAmbiguous = function() {
            return angular.isArray(this.model)
        }, b.prototype.isSharable = function() {
            return !this.isAmbiguous() && angular.isDefined(this.model.id)
        }, b
    }
]), angular.module("clotho.tokenizer").directive("clothoTokenizer", ["$parse", "clothoTokenCollectionFactory", "Debug",
    function(a, b, c) {
        var d = new c("clothoTokenizer", "#ee7711");
        return {
            restrict: "A",
            replace: !0,
            require: "ngModel",
            templateUrl: "html/commandViews/tokenizer.html",
            link: function(c, e, f, g) {
                function h() {
                    d.log("updating model (query, tokens)", k, c.tokenCollection.tokens), g.$setViewValue({
                        query: k,
                        tokens: c.tokenCollection.tokens
                    })
                }

                function i() {
                    k = "", c.tokenCollection.removeAll()
                }
                c.placeholder = f.placeholder;
                var j = a(f.startingTags)(c),
                    k = "";
                c.tokenCollection = new b(j), g.$render = function() {
                    i(), h()
                }, c.$watchCollection("tokenCollection.tokens", function() {
                    k = "", angular.forEach(c.tokenCollection.tokens, function(a) {
                        k += a.readable() + " "
                    }), h()
                }), c.addToken = function(a) {
                    d.log("TOKENIZER_LINK adding token", a), c.tokenCollection.addToken(a)
                }, c.removeToken = function(a) {
                    d.log("TOKENIZER_LINK removing token", a), c.tokenCollection.removeToken(a)
                }, c.tokenActive = function(a) {
                    return c.tokenCollection.isActive(a)
                }, c.toggleTokenActive = function(a, b) {
                    b.preventDefault(), c.tokenCollection.toggleActive(a)
                }, c.focusInput = function() {
                    e[0].querySelector("[clotho-autocomplete]").focus()
                }
            }
        }
    }
]);
