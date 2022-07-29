var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * JSweet implementation based on a local storage FS.
         */
        var FileInputStream = (function (_super) {
            __extends(FileInputStream, _super);
            function FileInputStream(name) {
                var _this = this;
                if (((typeof name === 'string') || name === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    {
                        var __args_1 = Array.prototype.slice.call(arguments);
                        var file_1 = name != null ? new java.io.File(name) : null;
                        _super.call(this);
                        Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.AutoCloseable"] });
                        this.index = 0;
                        (function () {
                            var name = (file_1 != null ? file_1.getPath() : null);
                            if (name == null) {
                                throw new java.lang.NullPointerException();
                            }
                            if (file_1.isInvalid()) {
                                throw new java.io.FileNotFoundException("Invalid file path");
                            }
                            if (!file_1.exists()) {
                                throw new java.io.FileNotFoundException();
                            }
                            _this.content = atob(java.io.LocalStorageFileSystem.fs_$LI$().getEntry(file_1.getAbsolutePath()).data);
                            _this.index = 0;
                        })();
                    }
                }
                else if (((name != null && name instanceof java.io.File) || name === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    var file_2 = __args[0];
                    _super.call(this);
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.AutoCloseable"] });
                    this.index = 0;
                    (function () {
                        var name = (file_2 != null ? file_2.getPath() : null);
                        if (name == null) {
                            throw new java.lang.NullPointerException();
                        }
                        if (file_2.isInvalid()) {
                            throw new java.io.FileNotFoundException("Invalid file path");
                        }
                        if (!file_2.exists()) {
                            throw new java.io.FileNotFoundException();
                        }
                        _this.content = atob(java.io.LocalStorageFileSystem.fs_$LI$().getEntry(file_2.getAbsolutePath()).data);
                        _this.index = 0;
                    })();
                }
                else
                    throw new Error('invalid overload');
            }
            FileInputStream.prototype.read$ = function () {
                if (this.index >= this.content.length) {
                    return -1;
                }
                return this.content[this.index++];
            };
            FileInputStream.prototype.readBytes = function (b, off, len) {
                if (this.index >= this.content.length) {
                    return -1;
                }
                var count = 0;
                for (var i = off; i < off + len; i++) {
                    if (this.index >= this.content.length) {
                        break;
                    }
                    b[i] = this.content[this.index++];
                    count++;
                }
                return count;
            };
            FileInputStream.prototype.read$byte_A = function (b) {
                return this.readBytes(b, 0, b.length);
            };
            FileInputStream.prototype.read = function (b, off, len) {
                var _this = this;
                if (((b != null && b instanceof Array) || b === null) && ((typeof off === 'number') || off === null) && ((typeof len === 'number') || len === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return (function () {
                        return _this.readBytes(b, off, len);
                    })();
                }
                else if (((b != null && b instanceof Array) || b === null) && off === undefined && len === undefined) {
                    return this.read$byte_A(b);
                }
                else if (b === undefined && off === undefined && len === undefined) {
                    return this.read$();
                }
                else
                    throw new Error('invalid overload');
            };
            FileInputStream.prototype.skip = function (n) {
                this.index += n;
                return n;
            };
            FileInputStream.prototype.available = function () {
                return this.content.length - this.index;
            };
            FileInputStream.prototype.close = function () {
            };
            return FileInputStream;
        }(java.io.InputStream));
        io.FileInputStream = FileInputStream;
        FileInputStream["__classname"] = "java.io.FileInputStream";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * JSweet implementation.
         */
        var FileNotFoundException = (function (_super) {
            __extends(FileNotFoundException, _super);
            function FileNotFoundException(s) {
                if (((typeof s === 'string') || s === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    _super.call(this, s);
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Serializable"] });
                }
                else if (s === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    _super.call(this);
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Serializable"] });
                }
                else
                    throw new Error('invalid overload');
            }
            FileNotFoundException.serialVersionUID = -897856973823710492;
            return FileNotFoundException;
        }(java.io.IOException));
        io.FileNotFoundException = FileNotFoundException;
        FileNotFoundException["__classname"] = "java.io.FileNotFoundException";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * JSweet partial implementation based on a local storage FS.
         */
        var FileOutputStream = (function (_super) {
            __extends(FileOutputStream, _super);
            function FileOutputStream(name, append) {
                var _this = this;
                if (((typeof name === 'string') || name === null) && ((typeof append === 'boolean') || append === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    {
                        var __args_2 = Array.prototype.slice.call(arguments);
                        var file_3 = name != null ? new java.io.File(name) : null;
                        _super.call(this);
                        Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                        this.append = false;
                        (function () {
                            if (!file_3.exists()) {
                                throw new java.io.FileNotFoundException();
                            }
                            _this.file = file_3;
                            _this.append = append;
                            _this.entry = java.io.LocalStorageFileSystem.fs_$LI$().getEntry(file_3.getAbsolutePath());
                            _this.content = append ? (_this.entry.data).split('').map(function (s) { return s.charCodeAt(0); }) : new Array(0);
                        })();
                    }
                }
                else if (((name != null && name instanceof java.io.File) || name === null) && ((typeof append === 'boolean') || append === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    var file_4 = __args[0];
                    _super.call(this);
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                    this.append = false;
                    (function () {
                        if (!file_4.exists()) {
                            throw new java.io.FileNotFoundException();
                        }
                        _this.file = file_4;
                        _this.append = append;
                        _this.entry = java.io.LocalStorageFileSystem.fs_$LI$().getEntry(file_4.getAbsolutePath());
                        _this.content = append ? (_this.entry.data).split('').map(function (s) { return s.charCodeAt(0); }) : new Array(0);
                    })();
                }
                else if (((typeof name === 'string') || name === null) && append === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    {
                        var __args_3 = Array.prototype.slice.call(arguments);
                        var file_5 = name != null ? new java.io.File(name) : null;
                        var append_1 = false;
                        _super.call(this);
                        Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                        this.append = false;
                        (function () {
                            if (!file_5.exists()) {
                                throw new java.io.FileNotFoundException();
                            }
                            _this.file = file_5;
                            _this.append = append_1;
                            _this.entry = java.io.LocalStorageFileSystem.fs_$LI$().getEntry(file_5.getAbsolutePath());
                            _this.content = append_1 ? (_this.entry.data).split('').map(function (s) { return s.charCodeAt(0); }) : new Array(0);
                        })();
                    }
                }
                else if (((name != null && name instanceof java.io.File) || name === null) && append === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    var file_6 = __args[0];
                    {
                        var __args_4 = Array.prototype.slice.call(arguments);
                        var append_2 = false;
                        _super.call(this);
                        Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                        this.append = false;
                        (function () {
                            if (!file_6.exists()) {
                                throw new java.io.FileNotFoundException();
                            }
                            _this.file = file_6;
                            _this.append = append_2;
                            _this.entry = java.io.LocalStorageFileSystem.fs_$LI$().getEntry(file_6.getAbsolutePath());
                            _this.content = append_2 ? (_this.entry.data).split('').map(function (s) { return s.charCodeAt(0); }) : new Array(0);
                        })();
                    }
                }
                else
                    throw new Error('invalid overload');
            }
            FileOutputStream.prototype.write$int$boolean = function (b, append) {
                (this.content).push((b | 0));
            };
            FileOutputStream.prototype.write$int = function (b) {
                this.write(b, this.append);
            };
            FileOutputStream.prototype.writeBytes = function (b, off, len, append) {
                for (var i = off; i < off + len; i++) {
                    (this.content).push(b[i]);
                }
            };
            FileOutputStream.prototype.write$byte_A = function (b) {
                this.writeBytes(b, 0, b.length, this.append);
            };
            FileOutputStream.prototype.write = function (b, off, len) {
                var _this = this;
                if (((b != null && b instanceof Array) || b === null) && ((typeof off === 'number') || off === null) && ((typeof len === 'number') || len === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return (function () {
                        _this.writeBytes(b, off, len, _this.append);
                    })();
                }
                else if (((typeof b === 'number') || b === null) && ((typeof off === 'boolean') || off === null) && len === undefined) {
                    return this.write$int$boolean(b, off);
                }
                else if (((b != null && b instanceof Array) || b === null) && off === undefined && len === undefined) {
                    return this.write$byte_A(b);
                }
                else if (((typeof b === 'number') || b === null) && off === undefined && len === undefined) {
                    return this.write$int(b);
                }
                else
                    throw new Error('invalid overload');
            };
            FileOutputStream.prototype.flush = function () {
                this.entry.data = btoa(((this.content).map(function (b, __, ___) {
                    return String.fromCharCode(b);
                })).join(""));
                java.io.LocalStorageFileSystem.fs_$LI$().putEntry(this.file.getAbsolutePath(), this.entry);
            };
            FileOutputStream.prototype.close = function () {
                this.flush();
            };
            return FileOutputStream;
        }(java.io.OutputStream));
        io.FileOutputStream = FileOutputStream;
        FileOutputStream["__classname"] = "java.io.FileOutputStream";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * JSweet implementation.
         */
        var FileReader = (function (_super) {
            __extends(FileReader, _super);
            function FileReader(fileName) {
                if (((typeof fileName === 'string') || fileName === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    _super.call(this, new java.io.FileInputStream(fileName));
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.Readable", "java.lang.AutoCloseable"] });
                }
                else if (((fileName != null && fileName instanceof java.io.File) || fileName === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    var file = __args[0];
                    _super.call(this, new java.io.FileInputStream(file));
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.io.Closeable", "java.lang.Readable", "java.lang.AutoCloseable"] });
                }
                else
                    throw new Error('invalid overload');
            }
            return FileReader;
        }(java.io.InputStreamReader));
        io.FileReader = FileReader;
        FileReader["__classname"] = "java.io.FileReader";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * Package-private abstract class for the local filesystem abstraction.
         */
        var FileSystem = (function () {
            function FileSystem() {
            }
            FileSystem.__static_initialize = function () { if (!FileSystem.__static_initialized) {
                FileSystem.__static_initialized = true;
                FileSystem.__static_initializer_0();
            } };
            FileSystem.prototype.normalize = function (pathname, len, off) {
                if (((typeof pathname === 'string') || pathname === null) && len === undefined && off === undefined) {
                    return this.normalize$java_lang_String(pathname);
                }
                else
                    throw new Error('invalid overload');
            };
            /**
             * Convert the given pathname string to normal form.  If the string is
             * already in normal form then it is simply returned.
             */
            FileSystem.prototype.normalize$java_lang_String = function (path) { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); };
            /**
             * Resolve the child pathname string against the parent.
             * Both strings must be in normal form, and the result
             * will be in normal form.
             */
            FileSystem.prototype.resolve = function (parent, child) {
                if (((typeof parent === 'string') || parent === null) && ((typeof child === 'string') || child === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return null;
                }
                else if (((parent != null && parent instanceof java.io.File) || parent === null) && child === undefined) {
                    return this.resolve$java_io_File(parent);
                }
                else
                    throw new Error('invalid overload');
            };
            /**
             * Resolve the given abstract pathname into absolute form.  Invoked by the
             * getAbsolutePath and getCanonicalPath methods in the File class.
             */
            FileSystem.prototype.resolve$java_io_File = function (f) { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); };
            FileSystem.getBooleanProperty = function (prop, defaultVal) {
                var val = java.lang.System.getProperty(prop);
                if (val == null)
                    return defaultVal;
                if ((function (o1, o2) { return o1.toUpperCase() === (o2 === null ? o2 : o2.toUpperCase()); })(val, "true")) {
                    return true;
                }
                else {
                    return false;
                }
            };
            FileSystem.__static_initializer_0 = function () {
                FileSystem.useCanonCaches = FileSystem.getBooleanProperty("sun.io.useCanonCaches", FileSystem.useCanonCaches);
                FileSystem.useCanonPrefixCache = FileSystem.getBooleanProperty("sun.io.useCanonPrefixCache", FileSystem.useCanonPrefixCache);
            };
            FileSystem.__static_initialized = false;
            FileSystem.BA_EXISTS = 1;
            FileSystem.BA_REGULAR = 2;
            FileSystem.BA_DIRECTORY = 4;
            FileSystem.BA_HIDDEN = 8;
            FileSystem.ACCESS_READ = 4;
            FileSystem.ACCESS_WRITE = 2;
            FileSystem.ACCESS_EXECUTE = 1;
            FileSystem.SPACE_TOTAL = 0;
            FileSystem.SPACE_FREE = 1;
            FileSystem.SPACE_USABLE = 2;
            FileSystem.useCanonCaches = true;
            FileSystem.useCanonPrefixCache = true;
            return FileSystem;
        }());
        io.FileSystem = FileSystem;
        FileSystem["__classname"] = "java.io.FileSystem";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * JSweet implementation.
         */
        var FileWriter = (function (_super) {
            __extends(FileWriter, _super);
            function FileWriter(fileName, append) {
                if (((typeof fileName === 'string') || fileName === null) && ((typeof append === 'boolean') || append === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    _super.call(this, new java.io.FileOutputStream(fileName, append));
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Appendable", "java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                }
                else if (((fileName != null && fileName instanceof java.io.File) || fileName === null) && ((typeof append === 'boolean') || append === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    var file = __args[0];
                    _super.call(this, new java.io.FileOutputStream(file, append));
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Appendable", "java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                }
                else if (((typeof fileName === 'string') || fileName === null) && append === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    _super.call(this, new java.io.FileOutputStream(fileName));
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Appendable", "java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                }
                else if (((fileName != null && fileName instanceof java.io.File) || fileName === null) && append === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    var file = __args[0];
                    _super.call(this, new java.io.FileOutputStream(file));
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Appendable", "java.io.Closeable", "java.lang.AutoCloseable", "java.io.Flushable"] });
                }
                else
                    throw new Error('invalid overload');
            }
            return FileWriter;
        }(java.io.OutputStreamWriter));
        io.FileWriter = FileWriter;
        FileWriter["__classname"] = "java.io.FileWriter";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var test;
(function (test) {
    var TestFile = (function () {
        function TestFile() {
        }
        TestFile.assertEquals = function (o1, o2) {
            if (!(o1 === o2)) {
                throw new Error("invalid assertion: " + o1 + "!=" + o2);
            }
        };
        TestFile.assertTrue = function (b) {
            if (!b) {
                throw new Error("invalid assertion");
            }
        };
        TestFile.assertFalse = function (b) {
            if (b) {
                throw new Error("invalid assertion");
            }
        };
        TestFile.test = function () {
            try {
                TestFile.testIO();
                var result = document.getElementById("result");
                if (result != null) {
                    result.innerHTML = "Success!";
                }
            }
            catch (e) {
                console.error(e);
                var result = document.getElementById("result");
                if (result != null) {
                    result.innerHTML = "Failure: " + e.message;
                }
            }
            ;
        };
        TestFile.testIO = function () {
            console.info("testing io");
            localStorage.clear();
            var s = new java.io.ByteArrayInputStream(/* getBytes */ ("abc").split('').map(function (s) { return s.charCodeAt(0); }));
            TestFile.assertEquals(javaemul.internal.CharacterHelper.getNumericValue('a'), s.read());
            var dir = new java.io.File("/a/b/c");
            TestFile.assertFalse(dir.exists());
            dir.mkdirs();
            TestFile.assertTrue(dir.exists());
            var f = new java.io.File(dir, "test.txt");
            TestFile.assertFalse(f.exists());
            f.createNewFile();
            TestFile.assertTrue(f.exists());
            var fw = new java.io.FileWriter(f);
            fw.append("abc");
            fw.close();
            var reader = new java.io.BufferedReader(new java.io.FileReader(f));
            var line = reader.readLine();
            reader.close();
            TestFile.assertEquals("abc", line);
            console.info("end testing io");
        };
        return TestFile;
    }());
    test.TestFile = TestFile;
    TestFile["__classname"] = "test.TestFile";
})(test || (test = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        var LocalStorageFileSystem = (function (_super) {
            __extends(LocalStorageFileSystem, _super);
            function LocalStorageFileSystem() {
                _super.call(this);
                this.PREFIX = "LSFS://";
            }
            LocalStorageFileSystem.fs_$LI$ = function () { if (LocalStorageFileSystem.fs == null)
                LocalStorageFileSystem.fs = new LocalStorageFileSystem(); return LocalStorageFileSystem.fs; };
            ;
            LocalStorageFileSystem.prototype.getSeparator = function () {
                return '/';
            };
            LocalStorageFileSystem.prototype.getPathSeparator = function () {
                return ':';
            };
            LocalStorageFileSystem.prototype.normalize = function (pathname, len, off) {
                if (((typeof pathname === 'string') || pathname === null) && ((typeof len === 'number') || len === null) && ((typeof off === 'number') || off === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return (function () {
                        if (len === 0)
                            return pathname;
                        var n = len;
                        while (((n > 0) && (pathname.charAt(n - 1) === '/')))
                            n--;
                        if (n === 0)
                            return "/";
                        var sb = new java.lang.StringBuffer(pathname.length);
                        if (off > 0)
                            sb.append(pathname.substring(0, off));
                        var prevChar = String.fromCharCode(0);
                        for (var i = off; i < n; i++) {
                            var c = pathname.charAt(i);
                            if ((prevChar === '/') && (c === '/'))
                                continue;
                            sb.append(c);
                            prevChar = c;
                        }
                        return sb.toString();
                    })();
                }
                else if (((typeof pathname === 'string') || pathname === null) && len === undefined && off === undefined) {
                    return this.normalize$java_lang_String(pathname);
                }
                else
                    throw new Error('invalid overload');
            };
            LocalStorageFileSystem.prototype.normalize$java_lang_String = function (pathname) {
                var n = pathname.length;
                var prevChar = String.fromCharCode(0);
                for (var i = 0; i < n; i++) {
                    var c = pathname.charAt(i);
                    if ((prevChar === '/') && (c === '/'))
                        return this.normalize(pathname, n, i - 1);
                    prevChar = c;
                }
                if (prevChar === '/')
                    return this.normalize(pathname, n, n - 1);
                return pathname;
            };
            LocalStorageFileSystem.prototype.prefixLength = function (pathname) {
                if (pathname.length === 0)
                    return 0;
                return (pathname.charAt(0) === '/') ? 1 : 0;
            };
            LocalStorageFileSystem.prototype.resolve = function (parent, child) {
                if (((typeof parent === 'string') || parent === null) && ((typeof child === 'string') || child === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return (function () {
                        if ((child === ""))
                            return parent;
                        if (child.charAt(0) === '/') {
                            if ((parent === "/"))
                                return child;
                            return parent + child;
                        }
                        if ((parent === "/"))
                            return parent + child;
                        return parent + '/' + child;
                    })();
                }
                else if (((parent != null && parent instanceof java.io.File) || parent === null) && child === undefined) {
                    return this.resolve$java_io_File(parent);
                }
                else
                    throw new Error('invalid overload');
            };
            LocalStorageFileSystem.prototype.getDefaultParent = function () {
                return "/";
            };
            LocalStorageFileSystem.prototype.fromURIPath = function (path) {
                var p = path;
                if ((function (str, searchString) { var pos = str.length - searchString.length; var lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(p, "/") && (p.length > 1)) {
                    p = p.substring(0, p.length - 1);
                }
                return p;
            };
            LocalStorageFileSystem.prototype.isAbsolute = function (f) {
                return (f.getPrefixLength() !== 0);
            };
            LocalStorageFileSystem.prototype.resolve$java_io_File = function (f) {
                if (this.isAbsolute(f))
                    return f.getPath();
                return this.resolve(java.lang.System.getProperty("user.dir"), f.getPath());
            };
            LocalStorageFileSystem.prototype.canonicalize = function (path) {
                return this.normalize(path);
            };
            LocalStorageFileSystem.prototype.getBooleanAttributes = function (f) {
                var e = this.getEntry(f.getAbsolutePath());
                return e == null ? 0 : e.attributes;
            };
            LocalStorageFileSystem.prototype.checkAccess = function (f, access) {
                return (this.getEntry(f.getAbsolutePath()).access & access) !== 0;
            };
            LocalStorageFileSystem.prototype.setPermission = function (f, access, enable, owneronly) {
                return false;
            };
            LocalStorageFileSystem.prototype.getLastModifiedTime = function (f) {
                return this.getEntry(f.getAbsolutePath()).lastModifiedTime;
            };
            LocalStorageFileSystem.prototype.getLength = function (f) {
                return this.getEntry(f.getAbsolutePath()).length;
            };
            LocalStorageFileSystem.prototype.clear = function () {
                for (var i = 0; i < localStorage.length; i++) {
                    var key = localStorage.key(i);
                    if ((function (str, searchString, position) {
                        if (position === void 0) { position = 0; }
                        return str.substr(position, searchString.length) === searchString;
                    })(key, this.PREFIX)) {
                        localStorage.removeItem(key);
                    }
                }
            };
            LocalStorageFileSystem.prototype.getKey = function (pathname) {
                return this.PREFIX + pathname;
            };
            LocalStorageFileSystem.prototype.createFileEntry = function (pathname) {
                var f = new java.io.File(pathname);
                pathname = f.getAbsolutePath();
                if (this.hasEntry(pathname)) {
                    return null;
                }
                var parent = f.getParentFile();
                if (parent != null) {
                    var parentPath = parent.getAbsolutePath();
                    var directoryEntry = this.getDirectoryEntry(parentPath);
                    if (directoryEntry == null) {
                        throw new java.io.IOException("directory does not exist: " + parentPath);
                    }
                    var entries = directoryEntry.entries;
                    entries.push(f.getName());
                    this.putEntry(parentPath, directoryEntry);
                }
                var e;
                this.putEntry(pathname, e = Object.defineProperty({
                    lastModifiedTime: java.lang.System.currentTimeMillis(),
                    length: 0,
                    data: "",
                    attributes: java.io.FileSystem.BA_EXISTS | java.io.FileSystem.BA_REGULAR,
                    access: java.io.FileSystem.ACCESS_READ | java.io.FileSystem.ACCESS_WRITE
                }, '__interfaces', { configurable: true, value: ["java.io.LocalStorageFileSystem.Entry"] }));
                return e;
            };
            LocalStorageFileSystem.prototype.createFileExclusively = function (pathname) {
                var e = this.createFileEntry(pathname);
                return e != null;
            };
            LocalStorageFileSystem.prototype.hasEntry = function (pathname) {
                return localStorage.getItem(this.getKey(pathname)) != null;
            };
            LocalStorageFileSystem.prototype.getEntry = function (pathname) {
                return JSON.parse(localStorage.getItem(this.getKey(pathname)));
            };
            LocalStorageFileSystem.prototype.getDirectoryEntry = function (pathname) {
                return JSON.parse(localStorage.getItem(this.getKey(pathname)));
            };
            LocalStorageFileSystem.prototype.putEntry = function (pathname, entry) {
                localStorage.setItem(this.getKey(pathname), JSON.stringify(entry));
            };
            LocalStorageFileSystem.prototype.getChildEntries = function (pathname) {
                var directoryEntry = this.getDirectoryEntry(pathname);
                if (directoryEntry != null) {
                    return directoryEntry.entries;
                }
                else {
                    return new Array();
                }
            };
            LocalStorageFileSystem.prototype.removeEntry = function (pathname) {
                {
                    var array122 = this.getChildEntries(pathname);
                    for (var index121 = 0; index121 < array122.length; index121++) {
                        var e = array122[index121];
                        {
                            this.removeEntry(pathname + "/" + e);
                        }
                    }
                }
                localStorage.removeItem(this.getKey(pathname));
            };
            LocalStorageFileSystem.prototype.delete = function (f) {
                if (this.hasEntry(f.getAbsolutePath())) {
                    this.removeEntry(f.getAbsolutePath());
                    var parentPath = f.getParentFile().getAbsolutePath();
                    var directoryEntry = this.getDirectoryEntry(parentPath);
                    var entries = directoryEntry.entries;
                    directoryEntry.entries = entries.splice(entries.indexOf(f.getName()), 1);
                    this.putEntry(parentPath, directoryEntry);
                    return true;
                }
                return false;
            };
            LocalStorageFileSystem.prototype.list = function (f) {
                return this.getChildEntries(f.getAbsolutePath());
            };
            LocalStorageFileSystem.prototype.createDirectory = function (f) {
                if (this.hasEntry(f.getAbsolutePath())) {
                    return false;
                }
                var parent = f.getParentFile();
                if (parent != null) {
                    var parentPath = parent.getAbsolutePath();
                    var directoryEntry = this.getDirectoryEntry(parentPath);
                    if (directoryEntry == null) {
                        return false;
                    }
                    var entries = directoryEntry.entries;
                    entries.push(f.getName());
                    this.putEntry(parentPath, directoryEntry);
                }
                this.putEntry(f.getAbsolutePath(), Object.defineProperty({
                    attributes: java.io.FileSystem.BA_DIRECTORY | java.io.FileSystem.BA_EXISTS,
                    access: java.io.FileSystem.ACCESS_READ | java.io.FileSystem.ACCESS_WRITE,
                    entries: new Array(0)
                }, '__interfaces', { configurable: true, value: ["java.io.LocalStorageFileSystem.DirectoryEntry", "java.io.LocalStorageFileSystem.Entry"] }));
                return true;
            };
            LocalStorageFileSystem.prototype.rename = function (f1, f2) {
                var e1 = this.getEntry(f1.getAbsolutePath());
                var e2 = this.getEntry(f2.getAbsolutePath());
                if (e1 == null || e2 != null) {
                    return false;
                }
                this.delete(f1);
                try {
                    this.createFileExclusively(f2.getAbsolutePath());
                }
                catch (e) {
                    return false;
                }
                ;
                this.putEntry(f2.getAbsolutePath(), e1);
                return true;
            };
            LocalStorageFileSystem.prototype.setLastModifiedTime = function (f, time) {
                var e = this.getEntry(f.getAbsolutePath());
                if (e != null) {
                    e.lastModifiedTime = time;
                    return true;
                }
                else {
                    return false;
                }
            };
            LocalStorageFileSystem.prototype.setReadOnly = function (f) {
                this.getEntry(f.getAbsolutePath()).access &= ~java.io.FileSystem.ACCESS_WRITE;
                return true;
            };
            LocalStorageFileSystem.prototype.listRoots = function () {
                if (this.roots == null) {
                    this.roots = [new java.io.File("/")];
                }
                return this.roots;
            };
            LocalStorageFileSystem.prototype.getSpace = function (f, t) {
                return 0;
            };
            LocalStorageFileSystem.prototype.compare = function (f1, f2) {
                return f1.getAbsolutePath().localeCompare(f2.getAbsolutePath());
            };
            LocalStorageFileSystem.prototype.hashCode = function (f) {
                return f.getAbsolutePath().toString();
            };
            return LocalStorageFileSystem;
        }(java.io.FileSystem));
        io.LocalStorageFileSystem = LocalStorageFileSystem;
        LocalStorageFileSystem["__classname"] = "java.io.LocalStorageFileSystem";
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var java;
(function (java) {
    var io;
    (function (io) {
        /**
         * JSweet implementation for file.
         */
        var File = (function () {
            function File(parent, child, direct) {
                var _this = this;
                this.status = null;
                if (((parent != null && parent instanceof java.io.File) || parent === null) && ((typeof child === 'string') || child === null) && ((typeof direct === 'boolean') || direct === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    this.status = null;
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Comparable", "java.io.Serializable"] });
                    this.prefixLength = 0;
                    (function () {
                        _this.path = java.io.LocalStorageFileSystem.fs_$LI$().resolve(parent.path, child);
                        _this.prefixLength = parent.prefixLength;
                    })();
                }
                else if (((typeof parent === 'string') || parent === null) && ((typeof child === 'string') || child === null) && direct === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    this.status = null;
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Comparable", "java.io.Serializable"] });
                    this.prefixLength = 0;
                    (function () {
                        if (child == null) {
                            throw new java.lang.NullPointerException();
                        }
                        if (parent != null) {
                            if ((parent === "")) {
                                _this.path = java.io.LocalStorageFileSystem.fs_$LI$().resolve(java.io.LocalStorageFileSystem.fs_$LI$().getDefaultParent(), java.io.LocalStorageFileSystem.fs_$LI$().normalize(child));
                            }
                            else {
                                _this.path = java.io.LocalStorageFileSystem.fs_$LI$().resolve(java.io.LocalStorageFileSystem.fs_$LI$().normalize(parent), java.io.LocalStorageFileSystem.fs_$LI$().normalize(child));
                            }
                        }
                        else {
                            _this.path = java.io.LocalStorageFileSystem.fs_$LI$().normalize(child);
                        }
                        _this.prefixLength = java.io.LocalStorageFileSystem.fs_$LI$().prefixLength(_this.path);
                    })();
                }
                else if (((parent != null && parent instanceof java.io.File) || parent === null) && ((typeof child === 'string') || child === null) && direct === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    this.status = null;
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Comparable", "java.io.Serializable"] });
                    this.prefixLength = 0;
                    (function () {
                        if (child == null) {
                            throw new java.lang.NullPointerException();
                        }
                        if (parent != null) {
                            if ((parent.path === "")) {
                                _this.path = java.io.LocalStorageFileSystem.fs_$LI$().resolve(java.io.LocalStorageFileSystem.fs_$LI$().getDefaultParent(), java.io.LocalStorageFileSystem.fs_$LI$().normalize(child));
                            }
                            else {
                                _this.path = java.io.LocalStorageFileSystem.fs_$LI$().resolve(parent.path, java.io.LocalStorageFileSystem.fs_$LI$().normalize(child));
                            }
                        }
                        else {
                            _this.path = java.io.LocalStorageFileSystem.fs_$LI$().normalize(child);
                        }
                        _this.prefixLength = java.io.LocalStorageFileSystem.fs_$LI$().prefixLength(_this.path);
                    })();
                }
                else if (((typeof parent === 'string') || parent === null) && ((typeof child === 'number') || child === null) && direct === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    var pathname_1 = __args[0];
                    var prefixLength_1 = __args[1];
                    this.status = null;
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Comparable", "java.io.Serializable"] });
                    this.prefixLength = 0;
                    (function () {
                        _this.path = pathname_1;
                        _this.prefixLength = prefixLength_1;
                    })();
                }
                else if (((typeof parent === 'string') || parent === null) && child === undefined && direct === undefined) {
                    var __args = Array.prototype.slice.call(arguments);
                    var pathname_2 = __args[0];
                    this.status = null;
                    Object.defineProperty(this, '__interfaces', { configurable: true, value: ["java.lang.Comparable", "java.io.Serializable"] });
                    this.prefixLength = 0;
                    (function () {
                        if (pathname_2 == null) {
                            throw new java.lang.NullPointerException();
                        }
                        _this.path = java.io.LocalStorageFileSystem.fs_$LI$().normalize(pathname_2);
                        _this.prefixLength = java.io.LocalStorageFileSystem.fs_$LI$().prefixLength(_this.path);
                    })();
                }
                else
                    throw new Error('invalid overload');
            }
            File.prototype.isInvalid = function () {
                if (this.status == null) {
                    this.status = (this.path.indexOf('\u0000') < 0) ? File.PathStatus.CHECKED : File.PathStatus.INVALID;
                }
                return this.status === File.PathStatus.INVALID;
            };
            File.prototype.getPrefixLength = function () {
                return this.prefixLength;
            };
            File.separatorChar_$LI$ = function () { if (File.separatorChar == null)
                File.separatorChar = java.io.LocalStorageFileSystem.fs_$LI$().getSeparator(); return File.separatorChar; };
            ;
            File.separator_$LI$ = function () { if (File.separator == null)
                File.separator = "" + File.separatorChar_$LI$(); return File.separator; };
            ;
            File.pathSeparatorChar_$LI$ = function () { if (File.pathSeparatorChar == null)
                File.pathSeparatorChar = java.io.LocalStorageFileSystem.fs_$LI$().getPathSeparator(); return File.pathSeparatorChar; };
            ;
            File.pathSeparator_$LI$ = function () { if (File.pathSeparator == null)
                File.pathSeparator = "" + File.pathSeparatorChar_$LI$(); return File.pathSeparator; };
            ;
            File.prototype.getName = function () {
                var index = this.path.lastIndexOf(File.separatorChar_$LI$());
                if (index < this.prefixLength)
                    return this.path.substring(this.prefixLength);
                return this.path.substring(index + 1);
            };
            File.prototype.getParent = function () {
                var index = this.path.lastIndexOf(File.separatorChar_$LI$());
                if (index < this.prefixLength) {
                    if ((this.prefixLength > 0) && (this.path.length > this.prefixLength))
                        return this.path.substring(0, this.prefixLength);
                    return null;
                }
                return this.path.substring(0, index);
            };
            File.prototype.getParentFile = function () {
                var p = this.getParent();
                if (p == null)
                    return null;
                return new File(p, this.prefixLength);
            };
            File.prototype.getPath = function () {
                return this.path;
            };
            File.prototype.isAbsolute = function () {
                return java.io.LocalStorageFileSystem.fs_$LI$().isAbsolute(this);
            };
            File.prototype.getAbsolutePath = function () {
                return java.io.LocalStorageFileSystem.fs_$LI$().resolve(this);
            };
            File.prototype.getAbsoluteFile = function () {
                var absPath = this.getAbsolutePath();
                return new File(absPath, java.io.LocalStorageFileSystem.fs_$LI$().prefixLength(absPath));
            };
            File.prototype.getCanonicalPath = function () {
                if (this.isInvalid()) {
                    throw new java.io.IOException("Invalid file path");
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().canonicalize(java.io.LocalStorageFileSystem.fs_$LI$().resolve(this));
            };
            File.prototype.getCanonicalFile = function () {
                var canonPath = this.getCanonicalPath();
                return new File(canonPath, java.io.LocalStorageFileSystem.fs_$LI$().prefixLength(canonPath));
            };
            File.slashify = function (path, isDirectory) {
                var p = path;
                if (File.separatorChar_$LI$() !== '/')
                    p = p.split(File.separatorChar_$LI$()).join('/');
                if (!(function (str, searchString, position) {
                    if (position === void 0) { position = 0; }
                    return str.substr(position, searchString.length) === searchString;
                })(p, "/"))
                    p = "/" + p;
                if (!(function (str, searchString) { var pos = str.length - searchString.length; var lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(p, "/") && isDirectory)
                    p = p + "/";
                return p;
            };
            File.prototype.canRead = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().checkAccess(this, java.io.FileSystem.ACCESS_READ);
            };
            File.prototype.canWrite = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().checkAccess(this, java.io.FileSystem.ACCESS_WRITE);
            };
            File.prototype.exists = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return ((java.io.LocalStorageFileSystem.fs_$LI$().getBooleanAttributes(this) & java.io.FileSystem.BA_EXISTS) !== 0);
            };
            File.prototype.isDirectory = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return ((java.io.LocalStorageFileSystem.fs_$LI$().getBooleanAttributes(this) & java.io.FileSystem.BA_DIRECTORY) !== 0);
            };
            File.prototype.isFile = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return ((java.io.LocalStorageFileSystem.fs_$LI$().getBooleanAttributes(this) & java.io.FileSystem.BA_REGULAR) !== 0);
            };
            File.prototype.isHidden = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return ((java.io.LocalStorageFileSystem.fs_$LI$().getBooleanAttributes(this) & java.io.FileSystem.BA_HIDDEN) !== 0);
            };
            File.prototype.lastModified = function () {
                if (this.isInvalid()) {
                    return 0;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().getLastModifiedTime(this);
            };
            File.prototype.length = function () {
                if (this.isInvalid()) {
                    return 0;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().getLength(this);
            };
            File.prototype.createNewFile = function () {
                if (this.isInvalid()) {
                    throw new java.io.IOException("Invalid file path");
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().createFileExclusively(this.path);
            };
            File.prototype.delete = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().delete(this);
            };
            File.prototype.list$ = function () {
                if (this.isInvalid()) {
                    return null;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().list(this);
            };
            File.prototype.list = function (filter) {
                var _this = this;
                if (((typeof filter === 'function' && filter.length == 2) || filter === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return (function () {
                        var names = _this.list();
                        if ((names == null) || (filter == null)) {
                            return names;
                        }
                        var v = new java.util.ArrayList();
                        for (var i = 0; i < names.length; i++) {
                            if (filter(_this, names[i])) {
                                v.add(names[i]);
                            }
                        }
                        return v.toArray(new Array(v.size()));
                    })();
                }
                else if (filter === undefined) {
                    return this.list$();
                }
                else
                    throw new Error('invalid overload');
            };
            File.prototype.listFiles$ = function () {
                var ss = this.list();
                if (ss == null)
                    return null;
                var n = ss.length;
                var fs = new Array(n);
                for (var i = 0; i < n; i++) {
                    fs[i] = new File(this, ss[i], true);
                }
                return fs;
            };
            File.prototype.listFiles = function (filter) {
                var _this = this;
                if (((typeof filter === 'function' && filter.length == 2) || filter === null)) {
                    var __args = Array.prototype.slice.call(arguments);
                    return (function () {
                        var ss = _this.list();
                        if (ss == null)
                            return null;
                        var files = new java.util.ArrayList();
                        for (var index123 = 0; index123 < ss.length; index123++) {
                            var s = ss[index123];
                            if ((filter == null) || filter(_this, s))
                                files.add(new File(_this, s, true));
                        }
                        return files.toArray(new Array(files.size()));
                    })();
                }
                else if (((typeof filter === 'function' && filter.length == 1) || filter === null)) {
                    return this.listFiles$java_io_FileFilter(filter);
                }
                else if (filter === undefined) {
                    return this.listFiles$();
                }
                else
                    throw new Error('invalid overload');
            };
            File.prototype.listFiles$java_io_FileFilter = function (filter) {
                var ss = this.list();
                if (ss == null)
                    return null;
                var files = new java.util.ArrayList();
                for (var index124 = 0; index124 < ss.length; index124++) {
                    var s = ss[index124];
                    {
                        var f = new File(this, s, true);
                        if ((filter == null) || filter(f))
                            files.add(f);
                    }
                }
                return files.toArray(new Array(files.size()));
            };
            File.prototype.mkdir = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().createDirectory(this);
            };
            File.prototype.mkdirs = function () {
                if (this.exists()) {
                    return false;
                }
                if (this.mkdir()) {
                    return true;
                }
                var canonFile = null;
                try {
                    canonFile = this.getCanonicalFile();
                }
                catch (e) {
                    return false;
                }
                ;
                var parent = canonFile.getParentFile();
                return (parent != null && (parent.mkdirs() || parent.exists()) && canonFile.mkdir());
            };
            File.prototype.renameTo = function (dest) {
                if (dest == null) {
                    throw new java.lang.NullPointerException();
                }
                if (this.isInvalid() || dest.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().rename(this, dest);
            };
            File.prototype.setLastModified = function (time) {
                if (time < 0)
                    throw new java.lang.IllegalArgumentException("Negative time");
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().setLastModifiedTime(this, time);
            };
            File.prototype.setReadOnly = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().setReadOnly(this);
            };
            File.prototype.setWritable = function (writable, ownerOnly) {
                if (ownerOnly === void 0) { ownerOnly = true; }
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().setPermission(this, java.io.FileSystem.ACCESS_WRITE, writable, ownerOnly);
            };
            File.prototype.setReadable = function (readable, ownerOnly) {
                if (ownerOnly === void 0) { ownerOnly = true; }
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().setPermission(this, java.io.FileSystem.ACCESS_READ, readable, ownerOnly);
            };
            File.prototype.setExecutable = function (executable, ownerOnly) {
                if (ownerOnly === void 0) { ownerOnly = true; }
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().setPermission(this, java.io.FileSystem.ACCESS_EXECUTE, executable, ownerOnly);
            };
            File.prototype.canExecute = function () {
                if (this.isInvalid()) {
                    return false;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().checkAccess(this, java.io.FileSystem.ACCESS_EXECUTE);
            };
            File.listRoots = function () {
                return java.io.LocalStorageFileSystem.fs_$LI$().listRoots();
            };
            File.prototype.getTotalSpace = function () {
                if (this.isInvalid()) {
                    return 0;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().getSpace(this, java.io.FileSystem.SPACE_TOTAL);
            };
            File.prototype.getFreeSpace = function () {
                if (this.isInvalid()) {
                    return 0;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().getSpace(this, java.io.FileSystem.SPACE_FREE);
            };
            File.prototype.getUsableSpace = function () {
                if (this.isInvalid()) {
                    return 0;
                }
                return java.io.LocalStorageFileSystem.fs_$LI$().getSpace(this, java.io.FileSystem.SPACE_USABLE);
            };
            File.createTempFile = function (prefix, suffix, directory) {
                if (directory === void 0) { directory = null; }
                if (prefix.length < 3)
                    throw new java.lang.IllegalArgumentException("Prefix string too short");
                if (suffix == null)
                    suffix = ".tmp";
                var tmpdir = (directory != null) ? directory : File.TempDirectory.location();
                var f;
                do {
                    f = File.TempDirectory.generateFile(prefix, suffix, tmpdir);
                } while (((java.io.LocalStorageFileSystem.fs_$LI$().getBooleanAttributes(f) & java.io.FileSystem.BA_EXISTS) !== 0));
                if (!java.io.LocalStorageFileSystem.fs_$LI$().createFileExclusively(f.getPath()))
                    throw new java.io.IOException("Unable to create temporary file");
                return f;
            };
            File.prototype.compareTo = function (pathname) {
                return java.io.LocalStorageFileSystem.fs_$LI$().compare(this, pathname);
            };
            File.prototype.equals = function (obj) {
                if ((obj != null) && (obj != null && obj instanceof java.io.File)) {
                    return this.compareTo(obj) === 0;
                }
                return false;
            };
            File.prototype.hashCode = function () {
                return java.io.LocalStorageFileSystem.fs_$LI$().hashCode(this);
            };
            File.prototype.toString = function () {
                return this.getPath();
            };
            File.serialVersionUID = 301077366599181567;
            return File;
        }());
        io.File = File;
        File["__classname"] = "java.io.File";
        var File;
        (function (File) {
            (function (PathStatus) {
                PathStatus[PathStatus["INVALID"] = 0] = "INVALID";
                PathStatus[PathStatus["CHECKED"] = 1] = "CHECKED";
            })(File.PathStatus || (File.PathStatus = {}));
            var PathStatus = File.PathStatus;
            var TempDirectory = (function () {
                function TempDirectory() {
                }
                TempDirectory.tmpdir_$LI$ = function () { if (TempDirectory.tmpdir == null)
                    TempDirectory.tmpdir = new java.io.File(java.lang.System.getProperty("java.io.tmpdir")); return TempDirectory.tmpdir; };
                ;
                TempDirectory.location = function () {
                    return TempDirectory.tmpdir_$LI$();
                };
                TempDirectory.generateFile = function (prefix, suffix, dir) {
                    var n = Math.round(Math.random()) * javaemul.internal.LongHelper.MAX_VALUE;
                    if (n === javaemul.internal.LongHelper.MIN_VALUE) {
                        n = 0;
                    }
                    else {
                        n = Math.abs(n);
                    }
                    prefix = (new java.io.File(prefix)).getName();
                    var name = prefix + ('' + n) + suffix;
                    var f = new java.io.File(dir, name);
                    if (!(name === f.getName()) || f.isInvalid()) {
                        throw new java.io.IOException("Unable to create temporary file, " + f);
                    }
                    return f;
                };
                return TempDirectory;
            }());
            File.TempDirectory = TempDirectory;
            TempDirectory["__classname"] = "java.io.File.TempDirectory";
        })(File = io.File || (io.File = {}));
    })(io = java.io || (java.io = {}));
})(java || (java = {}));
java.io.File.TempDirectory.tmpdir_$LI$();
java.io.File.pathSeparator_$LI$();
java.io.File.pathSeparatorChar_$LI$();
java.io.File.separator_$LI$();
java.io.File.separatorChar_$LI$();
java.io.LocalStorageFileSystem.fs_$LI$();
java.io.FileSystem.__static_initialize();
