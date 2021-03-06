var $clotho = window.$clotho = window.$clotho || {};
angular.module("clotho.core", ["clotho.angularAdditions"]), angular.module("clotho.angularAdditions", []).config(function() {
    var a = {};
    a.isEmpty = function(a) {
        if (angular.isNumber(a)) return a !== a;
        if (a === !0 || a === !1) return !1;
        if (angular.isObject(a)) {
            if (0 === a.length) return !0;
            for (var b in a)
                if (a.hasOwnProperty(b)) return !1;
            return !0
        }
        return angular.isUndefined(a) || null === a || 0 == a.length ? !0 : !1
    }, a.isScope = function(a) {
        return !!a && angular.isFunction(a.$evalAsync) && angular.isFunction(a.$watch)
    }, a.once = function(a) {
        var b, c;
        if (!angular.isFunction(a)) throw new TypeError;
        return function() {
            return b ? c : (b = !0, c = a.apply(this, arguments), a = null, c)
        }
    }, a.remove = function(a, b, c) {
        var d = -1,
            e = a ? a.length : 0,
            f = [];
        for (c = c || null; ++d < e;) {
            var g = a[d];
            (angular.isUndefined(g) || b.call(c, g, d, a)) && (angular.isDefined(g) && f.push(g), a.splice(d--, 1), e--)
        }
        return f
    }, a.map = function(a, b, c) {
        var d = angular.isArray(a) ? [] : {};
        return angular.forEach(a, function(a, e, f) {
            angular.isFunction(b) && (angular.isArray(d) ? d.push(b.call(c, a, e, f)) : d[e] = b.call(c, a, e, f))
        }), d
    }, angular.extend(angular, a)
}).run(["$rootScope",
    function(a) {
        a.$safeApply = function() {
            var a, b, c = !1;
            if (1 == arguments.length) {
                var d = arguments[0];
                "function" == typeof d ? b = d : a = d
            } else a = arguments[0], b = arguments[1], 3 == arguments.length && (c = !! arguments[2]);
            a = a || this, b = b || function() {}, c || !a.$$phase ? a.$apply ? a.$apply(b) : a.apply(b) : b()
        }
    }
]), angular.module("clotho.core").service("ClothoAuth", ["Clotho", "PubSub", "$q",
    function(a, b, c) {
        function d(a) {
            for (e = angular.isDefined(a) ? angular.copy(a) : null; f.length > 0;) {
                var b = f.pop();
                b.resolve(e)
            }
        }
        var e = null,
            f = (c.defer(), []);
        return b.on("auth:login auth:logout", d), {
            login: a.login,
            logout: a.logout,
            isLoggedIn: function() {
                return angular.isDefined(e)
            },
            getCurrentUser: function() {
                var a = $.defer();
                return angular.isDefined(e) ? a.resolve(e) : f.push(a), a.promise
            }
        }
    }
]).directive("clothoShowAuth", ["PubSub",
    function(a) {
        function b(a, b) {
            var c = a.$eval(b);
            return "string" == typeof c || angular.isArray(c) || (c = b), "string" == typeof c && (c = c.split(",")), c
        }

        function c(a, b) {
            var c = !1;
            return angular.forEach(b, function(b) {
                return b === a ? (c = !0, !0) : !1
            }), c
        }

        function d(a) {
            if (!a.length) throw new Error("clotho-show-auth directive must be login, logout, or error (you may use a comma-separated list)");
            return angular.forEach(a, function(a) {
                if (!c(a, ["login", "logout", "error"])) throw new Error('Invalid state "' + a + '" for clotho-show-auth directive, must be one of login, logout, or error')
            }), !0
        }
        var e = "logout";
        return a.on("auth:login", function() {
            e = "login"
        }), a.on("auth:logout", function() {
            e = "logout"
        }), a.on("auth:error", function() {
            e = "error"
        }), {
            restrict: "A",
            link: function(f, g, h) {
                function i() {
                    var a = c(e, j);
                    setTimeout(function() {
                        g.toggleClass("ng-cloak", !a)
                    }, 0)
                }
                var j = b(f, h.clothoShowAuth);
                d(j), i(), a.on("auth:login", i), a.on("auth:logout", i), a.on("auth:error", i)
            }
        }
    }
]), angular.module("clotho.core").factory("Debug", ["$log", "$window", "$filter",
    function(a, b, c) {
        function d(a) {
            return f === !0 ? !1 : g.indexOf(a) < 0
        }
        var e = !1,
            f = !1,
            g = [],
            h = {};
        return function(f, g) {
            f = angular.isString(f) ? f : "ANONYMOUS", g = /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i.test(g) ? g : "#" + Math.floor(16777215 * Math.random()).toString(16), h[f] = [];
            var i = {};
            return angular.forEach(["log", "warn", "error", "debug", "info"], function(b) {
                i[b] = function() {
                    var i = (new Error).stack;
                    angular.isDefined(i) && (i = i.split("\n"), 0 === i[0].indexOf("Error") && (i = i.slice(1)), i = i.slice(1));
                    var j = new Date,
                        k = c("date")(j, "hh:mm:ss.sss");
                    h[f].push({
                        message: arguments,
                        time: j.valueOf()
                    }), d(f) && (a[b].apply(null, ["%c" + k + " - %O - " + f + "	", "color: " + g + ";", i].concat(Array.prototype.slice.call(arguments, 0))), e && console.trace())
                }
            }), angular.forEach(["table"], function(a) {
                i[a] = b.console[a]
            }), i.object = function(b) {
                h[f].push({
                    message: b,
                    time: Date.now().valueOf()
                }), angular.isObject(b) ? a.log("%c" + JSON.stringify(b, null, 2), "color: " + g + ";") : a.log("%c" + b, "color: " + g + ";")
            }, i.group = function(a) {
                angular.isDefined(b.console.group) && b.console.group("%c" + f + "	" + a || "Collapsing Output", "color: " + g + ";")
            }, i.groupCollapsed = function(a) {
                angular.isDefined(b.console.groupCollapsed) && b.console.groupCollapsed("%c" + f + "	" + a || "Collapsing Output", "color: " + g + ";")
            }, i.groupEnd = function() {
                angular.isDefined(b.console.groupEnd) && b.console.groupEnd()
            }, i.$log = a, i.console = b.console || {}, i
        }
    }
]), angular.module("clotho.core").service("Clotho", ["Socket", "Collector", "PubSub", "Debug", "$window", "$q", "$rootScope", "$location", "$timeout",
    function(a, b, c, d, e, f, g, h, i) {
        function j() {
            function e() {
                var a = f.defer();
                return a.reject(), a.promise
            }
            var g = new d("ClothoAPI", "#cc5555"),
                j = {};
            j.send = function(b) {
                return a.send(JSON.stringify(b))
            }, j.pack = function(a, b, c, d) {
                return {
                    channel: a,
                    data: b,
                    requestId: c,
                    options: d
                }
            }, j.emit = function(a, b, c, d) {
                return j.send(j.pack(a, b, c, d))
            };
            var k = new function() {
                var a = 0;
                this.next = function() {
                    return a += 1, a.toString()
                }
            };
            j.emitSubCallback = function(a, b, d, e) {
                var g = f.defer(),
                    h = Date.now().toString() + k.next();
                angular.isFunction(d) || (d = angular.noop);
                var l = i(function() {
                    g.reject(null)
                }, 5e3);
                c.once(a + ":" + h, function(a) {
                    i.cancel(l), angular.isUndefined(a) ? g.reject(null) : g.resolve(a), d(a)
                }, "$clotho");
                var m = j.emit(a, b, h, e);
                return m === !1 && (i.cancel(l), g.reject(null)), g.promise
            }, j.emitSubOnce = function(a, b, c) {
                return j.emitSubCallback(a, b, angular.noop, c)
            };
            var l = function(a, b) {
                function d(a) {
                    a ? c.trigger("auth:login", a) : c.trigger("auth:error", "Could not Login")
                }
                var e = {
                    username: a,
                    credentials: b
                };
                return j.emitSubCallback("login", e, d)
            }, m = function() {
                function a(a) {
                    a ? c.trigger("auth:logout", null) : c.trigger("auth:error", "Could not Logout")
                }
                return j.emitSubCallback("logout", "", a)
            }, n = function(a, b) {
                return o(a, b)
            }, o = function(a, c) {
                if (angular.isUndefined(a)) return e();
                var d = b.retrieveModel(a);
                if (d) return f.when(d);
                var g = function(c) {
                    b.storeModel(a, c)
                };
                return j.emitSubCallback("get", a, g, c)
            }, p = function(a) {
                if (angular.isEmpty(a)) return !1;
                var c = function() {
                    b.storeModel(a.id, a)
                };
                return j.emitSubCallback("set", a, c)
            }, q = function(a, b, d) {
                return d = "undefined" != typeof d ? d : null, c.on("update:" + a, function() {
                    g.log("watch triggered for " + a), n(a, {
                        mute: !0
                    }).then(function(a) {
                        angular.isFunction(b) ? b.apply(d, [a]) : angular.extend(b, a)
                    })
                }, d)
            }, r = function(a, b, d) {
                d = "undefined" != typeof d ? d : null, c.on(a, function(a) {
                    b(a)
                }, d)
            }, s = function(a, b) {
                c.trigger(a, b)
            }, t = function(a) {
                c.destroy(a)
            }, u = function(a, b) {
                j.emit(a, b || {})
            }, v = function(a, b) {
                j.emit("broadcast", j.pack(a, b))
            }, w = function(b, c) {
                return a.on(b, c)
            }, x = function(b, c) {
                return a.once(b, c)
            }, y = function(b) {
                a.off(b)
            }, z = function(a, c) {
                var d = function(c) {
                    console.groupCollapsed("Query Results for: " + JSON.stringify(a));
                    try {
                        angular.forEach(c, function(a) {
                            b.storeModel(a.id, a)
                        })
                    } catch (d) {
                        g.warn("error saving all models", d)
                    }
                    console.groupEnd()
                };
                return j.emitSubCallback("query", a, d, c)
            }, A = function(a) {
                return angular.isUndefined(a) ? null : (null === a.id && delete a.id, j.emitSubOnce("create", a))
            }, B = function(a) {
                if (angular.isUndefined(a)) return e();
                var c = function() {
                    b.removeModel(a)
                };
                return j.emitSubCallback("destroy", a, c)
            }, C = function(a) {
                h.path("/editor").search("id", a)
            }, D = function(a, b) {
                var c = {
                    uuid: a,
                    timestamp: b
                };
                return j.emitSubOnce("revert", c)
            }, E = function(a) {
                return angular.isEmpty(a) ? e() : j.emitSubOnce("validate", a)
            }, F = function(a, b) {
                var c = {
                    viewId: a,
                    options: b
                };
                j.emit("show", c)
            }, G = function() {
                //console.log("need to set up share")
            }, H = function(a) {
                j.emit("log", a)
            }, I = function(a, b) {
                var c = {
                    userID: b || "",
                    msg: a,
                    timestamp: Date.now(),
                    from: "client"
                };
                j.emit("say", c)
            }, J = function(a) {
                j.emit("notify", a)
            }, K = function(a, b) {
                var c = {
                    userID: b,
                    msg: a
                };
                j.emit("alert", c)
            }, L = function(a, b) {
                var c = {
                    query: a
                };
                return j.emitSubOnce("autocomplete", c, b)
            }, M = function(a) {
                return angular.isString(a) && (a = {
                    query: a,
                    tokens: []
                }), j.emitSubOnce("submit", a)
            }, N = function(a, b, c) {
                var d = {
                    id: a,
                    args: b
                };
                return c = angular.isObject(c) ? c : {}, j.emitSubOnce("run", d, c)
            }, O = function(a) {
                return j.emitSubOnce("recent", a)
            }, P = function(a, b) {
                h.path("/trail").search("id", a), angular.isDefined(b) && h.search("position", b)
            };
            return {
                login: l,
                logout: m,
                get: n,
                set: p,
                query: z,
                create: A,
                edit: C,
                validate: E,
                revert: D,
                destroy: B,
                show: F,
                say: I,
                log: H,
                alert: K,
                run: N,
                recent: O,
                notify: J,
                submit: M,
                autocomplete: L,
                watch: q,
                listen: r,
                silence: t,
                trigger: s,
                emit: u,
                broadcast: v,
                on: w,
                once: x,
                off: y,
                share: G,
                startTrail: P
            }
        }
        return e.$clotho.api ? e.$clotho.api : e.$clotho.api = j()
    }
]), angular.module("clotho.core").service("ClientAPI", ["PubSub", "Collector", "Debug", "$q", "$injector", "$window", "$templateCache", "$http", "$rootScope", "$location", "$document",
    function(a, b, c, d, e, f, g, h, i, j) {
        var k = new c("ClientAPI", "#dd99dd");
        if (e.has("$clothoModal")) var l = e.get("$clothoModal");
        var m = function(a) {
            var c = a,
                d = c.id || c.uuid || !1;
            b.storeModel(d, c)
        }, n = function(b) {
            a.trigger(b.channel, b.data)
        }, o = function(a, b) {
            var c = angular.element('<div clotho-show="' + a + '"></div>'),
                d = document.querySelector(b);
            d || (d = document.getElementById("clothoAppWidgets")), d = angular.element(d), d.append($compile(c)(i))
        }, p = function(a, b) {
            var c = angular.element(document.querySelector('[clotho-show="' + a + '"]'));
            b.apply(null, c.remove())
        }, q = function(a) {
            k.info(a)
        }, r = function(b) {
            angular.isString(b.text) || (angular.isNumber(b.text) ? b.text = parseInt(b.text) : null === b.text ? b.text = "null" : angular.isUndefined(b.text) ? b.text = "undefined" : b.text === !0 ? b.text = "true" : b.text === !1 && (b.text = "false"));
            var c = {
                "class": "muted",
                from: "server",
                timestamp: Date.now().valueOf()
            };
            b = angular.extend({}, c, b);
            var d = {
                success: "success",
                warning: "warning",
                error: "danger",
                failure: "danger",
                normal: "success",
                muted: "muted",
                info: "info"
            };
            b.class = d[angular.lowercase(b.class)], a.trigger("activityLog", b)
        }, s = function() {
            angular.isDefined("$clothoModal") && l.create({
                title: "Clotho Alert",
                content: "msg"
            })
        }, t = function() {}, u = function(b, c) {
            a.trigger("revisions:" + b, c)
        }, v = function(a) {
            j.path(a)
        }, w = function(a) {
            e.has("$route") ? j.path("/editor?id=" + a) : r({
                text: "Editor not available",
                from: "client",
                "class": "error"
            })
        }, x = function(a) {
            j.path("/trails/" + a)
        };
        return {
            collect: m,
            broadcast: n,
            log: q,
            say: r,
            alert: s,
            display: o,
            hide: p,
            help: t,
            revisions: u,
            changeUrl: v,
            edit: w,
            startTrail: x
        }
    }
]), angular.module("clotho.core").service("clothoLocalStorage", ["$window", "PubSub", "Debug", "$document",
    function(a, b, c, d) {
        function e() {
            function d(a, c) {
                b.trigger("update", a), b.trigger("update:" + a, angular.copy(c))
            }
            var e = new c("clothoLocalStorage", "#88cc88"),
                f = a.localStorage,
                g = JSON,
                h = "clotho_",
                i = function() {
                    try {
                        if (f.setItem("test", "7"), "7" === f.getItem("test")) return f.removeItem("test"), !0
                    } catch (a) {}
                    return !1
                };
            e.log("localStorage support? " + i());
            var j = function() {
                for (var a = !1, b = 0, c = f.length; c > b; ++b) {
                    var d = f.key(b);
                    angular.isString(d) && d.substring(0, h.length) == h && (f.removeItem(d), --b, --c, a = !0)
                }
                return a
            }, k = function(a, b) {
                if (angular.isEmpty(a)) return null;
                var c = f.getItem(h + a);
                return angular.isEmpty(c) ? angular.isDefined(b) ? b : null : angular.isObject(c) ? c : g.parse(c)
            }, l = function(a) {
                if (angular.isEmpty(a)) return !1;
                var b = f.getItem(h + a);
                return angular.isDefined(b) && !angular.isEmpty(b)
            }, m = function(a) {
                return angular.isEmpty(a) ? !1 : (f.removeItem(h + a), !0)
            }, n = function(a, b) {
                if (angular.isEmpty(a)) return !1;
                if (angular.isEmpty(b)) return !1;
                b = angular.isString(b) ? b : g.stringify(b);
                try {
                    f.setItem(h + a, b)
                } catch (c) {
                    e.error("couldnt save " + a, c)
                }
                return !0
            }, o = function(b) {
                if (b || (b = a.event), !(b.key.indexOf(h) < 0)) {
                    var c = b.key.replace(h, "") || "",
                        f = k(c);
                    e.log("handle_storage_event for " + c, f), d(c, f)
                }
            };
            return a.addEventListener ? a.addEventListener("storage", o, !1) : a.attachEvent("onstorage", o), {
                getPrefix: function() {
                    return h
                },
                isSupported: i,
                clear: j,
                getItem: k,
                hasItem: l,
                removeItem: m,
                setItem: n
            }
        }
        return a.$clotho.$localStorage ? a.$clotho.$localStorage : a.$clotho.$localStorage = e()
    }
]), angular.module("clotho.core").service("Collector", ["$window", "clothoLocalStorage", "PubSub", "Debug",
    function(a, b, c, d) {
        function e() {
            function a(a, b) {
                c.trigger("update", a), c.trigger("update:" + a, angular.copy(b))
            }
            var e = new d("Collector", "#55bb55"),
                f = function(a, c) {
                    b.setItem(a, c)
                }, g = function(b, c, d) {
                    d || !angular.equals(i(b), c) ? (e.log(b + " (saving)", c), f(b, c), a(b, c)) : e.log(b + " (model unchanged)")
                }, h = function(a) {
                    return angular.copy(i(a))
                }, i = function(a) {
                    return b.hasItem(a) ? b.getItem(a) : !1
                }, j = function(a) {
                    return b.hasItem(a) ? (b.removeItem(a), !0) : !1
                }, k = function() {
                    b.clear(), c.trigger("collector_reset")
                };
            return k(), {
                hasItem: b.hasItem,
                silentAddModel: f,
                storeModel: g,
                retrieveModel: h,
                retrieveRef: i,
                removeModel: j,
                clearStorage: k
            }
        }
        return a.$clotho.$collector ? a.$clotho.$collector : a.$clotho.$collector = e()
    }
]), angular.module("clotho.core").service("PubSub", ["$window", "$rootScope", "$filter", "Debug",
    function(a, b, c, d) {
        function e() {
            function a(a) {
                return a ? "all" == a ? Object.keys(k) : a.split(l) : []
            }

            function e(a) {
                return k.hasOwnProperty(a)
            }

            function f(a) {
                return e(a) && k[a].length > 0
            }

            function g(a, b, c, d) {
                return angular.isFunction(a) ? {
                    callback: a,
                    ref: b || null,
                    priority: parseInt(c) || 100,
                    once: 1 == d
                } : null
            }

            function h(a, b) {
                if (b && !angular.isEmpty(b)) {
                    k[a] || (k[a] = []);
                    var c = b.ref;
                    return angular.isScope(c) && c.$on("$destroy", function() {
                        t(j(c))
                    }), k[a].push(b), angular.once(function() {
                        i(a, b)
                    })
                }
                return angular.noop
            }

            function i(a, b) {
                var c = angular.remove(k[a], function(a) {
                    return angular.equals(a, b)
                });
                return c.length > 0
            }

            function j(a) {
                return angular.isScope(a) ? a.$id : a
            }
            var k = {}, l = /\s+/,
                m = new d("PubSub", "#55cccc"),
                n = function() {
                    m.log("LISTENERS:"), angular.forEach(k, function(a, b) {
                        m.log(b), m.table(k[b])
                    })
                }, o = function(d, e) {
                    e = angular.isUndefined(e) || null === e ? null : [e], angular.forEach(a(d), function(a) {
                        m.log("Publish on " + a, e), f(a) && angular.forEach(c("orderBy")(k[a], "priority"), function(c, d) {
                            b.$safeApply(function() {
                                c.callback.apply(c.ref, e)
                            }), 1 == c.once && k[a].splice(d, 1)
                        })
                    })
                }, p = function(c) {
                    angular.forEach(a(c), function(a) {
                        m.log("Reject on " + a), angular.forEach(k[a], function(c, d) {
                            b.$safeApply(function() {
                                c.callback.apply(c.ref, null)
                            }), 1 == c.once && k[a].splice(d, 1)
                        })
                    })
                }, q = function(b, c, d, e, f) {
                    d = d || null, f = 1 == f, f && (c = angular.once(c));
                    var i = [];
                    return angular.forEach(a(b), function(a) {
                        i.push(h(a, g(c, d, e, f)))
                    }),
                        function() {
                            angular.forEach(i, function(a) {
                                a()
                            })
                        }
                }, r = function(a, b, c, d) {
                    q(a, b, c, d, !0)
                }, s = function(a, b) {
                    var c = angular.remove(k[a], function(a) {
                        return angular.equals(a.callback, b)
                    });
                    return c.length > 0
                }, t = function(a) {
                    angular.forEach(k, function(b) {
                        angular.remove(b, function(b) {
                            return angular.equals(j(a), j(b.ref))
                        })
                    })
                }, u = function(b) {
                    angular.forEach(a(b), function(a, b) {
                        k[b].length = 0
                    })
                };
            return {
                logListeners: n,
                trigger: o,
                on: q,
                once: r,
                off: s,
                destroy: t,
                reject: p,
                clear: u
            }
        }
        return a.$clotho.$pubsub ? a.$clotho.$pubsub : a.$clotho.$pubsub = e()
    }
]), angular.module("clotho.core").service("Socket", ["$window", "$q", "$log", "PubSub", "ClientAPI", "Debug",
    function(a, b, c, d, e, f) {
        function g() {
            function b(a) {
                return a.length < 16348
            }

            function c(a) {
                return i ? (a = angular.isObject(a) ? JSON.stringify(a) : a, b(a) ? (k.log("sending data: ", angular.isObject(a) ? a : JSON.parse(a)), h.send(a), !0) : (k.warn("Message did not pass validation!"), e.say({
                    "class": "error",
                    text: "Message could not be sent to server",
                    from: "client"
                }), !1)) : (k.log("(not ready) queueing request: ", a), void j.push(a))
            }

            function g() {
                k.group("Sending Socket Queue"), angular.forEach(j, function(a) {
                    c(a)
                }), k.groupEnd(), j = []
            }
            var h, i, j = [],
                k = new f("Socket", "#5555bb");
            if (a.$clotho.socket) h = a.$clotho.socket;
            else {
                var l = window.location.pathname,
                    m = l.substring(0, l.lastIndexOf("/"));
                /\.html/.test(m) && (m = m.substring(0, m.lastIndexOf("/")));
                var n = "https:" == window.location.protocol ? "wss:" : "ws:";
                h = a.$clotho.socket = new WebSocket(n + "//" + window.location.host  + "/websocket")
            }
            return 1 == h.readyState ? (k.log("already exists, sending items in socket Queue..."), i = !0, g()) : h.onopen = function() {
                k.log("opened, sending queued items..."), i = !0, g()
            }, h.onerror = function(a) {
                k.error("socket error", a)
            }, h.onclose = function(a) {
                i = !1, k.error("socket closed", a), e.say({
                    "class": "error",
                    text: "Socket Connection Closed (Please Reload)" + (a.reason ? " - " + a.reason : ""),
                    from: "client"
                })
            }, h.onmessage = function(a) {
                a = JSON.parse(a.data), k.log("received", a);
                var b = a.channel,
                    c = a.requestId,
                    f = angular.isUndefined(a.data),
                    g = a.data;
                if (angular.isFunction(e[b])) e[b](g);
                else {
                    var h = f ? "reject" : "trigger";
                    c ? d[h](b + ":" + c, g) : d[h](b, g)
                }
            }, {
                state: function() {
                    return h.readyState
                },
                emit: function(a, b, d) {
                    var e = {
                        channel: a,
                        data: b
                    }, f = c(e);
                    return f && "function" == typeof d && d(e), f
                },
                send: c
            }
        }
        return a.$clotho.$socket ? a.$clotho.$socket : a.$clotho.$socket = g()
    }
]);
