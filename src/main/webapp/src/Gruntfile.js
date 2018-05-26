'use strict';

module.exports = function(grunt) {

  grunt.initConfig({

    // get packge info
    pkg: grunt.file.readJSON('package.json'),

    // **MANUFACTORY OPEN*****************************************************
    // app
    mnf_open_app: 'app/manufactory/open/js/app.js',

    // --------------------------------------------
    // get all states / modules
    mnf_open_states: grunt.file.expand(
      'app/manufactory/open/states/**/*.js',
      '!app/manufactory/open/states/**/*.pageobject.js',
      '!app/manufactory/open/states/**/*.scenario.js',
      '!app/manufactory/open/states/**/*.spec.js'
    ).join(' '),

    // get all components
    mnf_open_components: grunt.file.expand(
      'app/manufactory/open/components/**/*.js',
      '!app/manufactory/open/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // mnf_open_pages: grunt.file.expand(
    //   'app/manufactory/open/**/*.html'
    // ).join(' '),

    // **MANUFACTORY SECURED**************************************************
    // app
    mnf_secured_app: 'app/manufactory/secured/js/app.js',

    // --------------------------------------------
    // get all states / modules
    mnf_secured_states: grunt.file.expand(
      'app/manufactory/secured/states/**/*.js',
      '!app/manufactory/secured/states/**/*.pageobject.js',
      '!app/manufactory/secured/states/**/*.scenario.js',
      '!app/manufactory/secured/states/**/*.spec.js'
    ).join(' '),

    // get all components
    mnf_secured_components: grunt.file.expand(
      'app/manufactory/secured/components/**/*.js',
      '!app/manufactory/secured/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // mnf_secured_pages: grunt.file.expand(
    //   'app/manufactory/secured/**/*.html'
    // ).join(' '),

    // **DOCTOR OPEN**********************************************************
    // app
    dct_open_app: 'app/doctor/open/js/app.js',

    // --------------------------------------------
    // get all states / modules
    dct_open_states: grunt.file.expand(
      'app/doctor/open/states/**/*.js',
      '!app/doctor/open/states/**/*.pageobject.js',
      '!app/doctor/open/states/**/*.scenario.js',
      '!app/doctor/open/states/**/*.spec.js'
    ).join(' '),

    // get all components
    dct_open_components: grunt.file.expand(
      'app/doctor/open/components/**/*.js',
      '!app/doctor/open/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // dct_open_pages: grunt.file.expand(
    //   'app/doctor/open/**/*.html'
    // ).join(' '),

    // **DOCTOR SECURED*******************************************************
    // app
    dct_secured_app: 'app/doctor/secured/js/app.js',

    // --------------------------------------------
    // get all states / modules
    dct_secured_states: grunt.file.expand(
      'app/doctor/secured/states/**/*.js',
      '!app/doctor/secured/states/**/*.pageobject.js',
      '!app/doctor/secured/states/**/*.scenario.js',
      '!app/doctor/secured/states/**/*.spec.js'
    ).join(' '),

    // get all components
    dct_secured_components: grunt.file.expand(
      'app/doctor/secured/components/**/*.js',
      '!app/doctor/secured/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // dct_secured_pages: grunt.file.expand(
    //   'app/doctor/secured/**/*.html'
    // ).join(' '),

    // **MANAGER OPEN*********************************************************
    // app
    mgr_open_app: 'app/manager/open/js/app.js',

    // --------------------------------------------
    // get all states / modules
    mgr_open_states: grunt.file.expand(
      'app/manager/open/states/**/*.js',
      '!app/manager/open/states/**/*.pageobject.js',
      '!app/manager/open/states/**/*.scenario.js',
      '!app/manager/open/states/**/*.spec.js'
    ).join(' '),

    // get all components
    mgr_open_components: grunt.file.expand(
      'app/manager/open/components/**/*.js',
      '!app/manager/open/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // mgr_open_pages: grunt.file.expand(
    //   'app/manager/open/**/*.html'
    // ).join(' '),

    // **MANAGER SECURED******************************************************
    // app
    mgr_secured_app: 'app/manager/secured/js/app.js',

    // --------------------------------------------
    // get all states / modules
    mgr_secured_states: grunt.file.expand(
      'app/manager/secured/states/**/*.js',
      '!app/manager/secured/states/**/*.pageobject.js',
      '!app/manager/secured/states/**/*.scenario.js',
      '!app/manager/secured/states/**/*.spec.js'
    ).join(' '),

    // get all components
    mgr_secured_components: grunt.file.expand(
      'app/manager/secured/components/**/*.js',
      '!app/manager/secured/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // mgr_secured_pages: grunt.file.expand(
    //   'app/manager/secured/**/*.html'
    // ).join(' '),

    // **SUPER OPEN***********************************************************
    // app
    spr_open_app: 'app/super/open/js/app.js',

    // --------------------------------------------
    // get all states / modules
    spr_open_states: grunt.file.expand(
      'app/super/open/states/**/*.js',
      '!app/super/open/states/**/*.pageobject.js',
      '!app/super/open/states/**/*.scenario.js',
      '!app/super/open/states/**/*.spec.js'
    ).join(' '),

    // get all components
    spr_open_components: grunt.file.expand(
      'app/super/open/components/**/*.js',
      '!app/super/open/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // spr_open_pages: grunt.file.expand(
    //   'app/super/open/**/*.html'
    // ).join(' '),

    // **SUPER SECURED********************************************************
    // app
    spr_secured_app: 'app/super/secured/js/app.js',

    // --------------------------------------------
    // get all states / modules
    spr_secured_states: grunt.file.expand(
      'app/super/secured/states/**/*.js',
      '!app/super/secured/states/**/*.pageobject.js',
      '!app/super/secured/states/**/*.scenario.js',
      '!app/super/secured/states/**/*.spec.js'
    ).join(' '),

    // get all components
    spr_secured_components: grunt.file.expand(
      'app/super/secured/components/**/*.js',
      '!app/super/secured/components/**/*.spec.js'
    ).join(' '),

    // get sub pages
    // spr_secured_pages: grunt.file.expand(
    //   'app/super/secured/**/*.html'
    // ).join(' '),

    // start local server
    connect: {
      server: {
        options: {
          port: 8080,
          base: 'app',
          keepalive: false,
          livereload: true
        }
      }
    },

    // run shell scripts
    shell: {

      // create app.min.js
      mnf_open_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= mnf_open_app %> ' +
          // '--js <%= mnf_open_states %> ' +
          '--js app/manufactory/open/states/**.js ' +
          '--js !app/manufactory/open/states/**.pageobject.js ' +
          '--js !app/manufactory/open/states/**.scenario.js ' +
          '--js !app/manufactory/open/states/**.spec.js ' +
          // '--js <%= mnf_open_components %> ' +
          '--js app/manufactory/open/components/**.js ' +
          '--js !app/manufactory/open/components/**.spec.js ' +
          '--js_output_file app/manufactory/open/js/app.min.js'
      },

      mnf_secured_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= mnf_secured_app %> ' +
          // '--js <%= mnf_secured_states %> ' +
          '--js app/manufactory/secured/states/**.js ' +
          '--js !app/manufactory/secured/states/**.pageobject.js ' +
          '--js !app/manufactory/secured/states/**.scenario.js ' +
          '--js !app/manufactory/secured/states/**.spec.js ' +
          // '--js <%= mnf_secured_components %> ' +
          '--js app/manufactory/secured/components/**.js ' +
          '--js !app/manufactory/secured/components/**.spec.js ' +
          '--js_output_file app/manufactory/secured/js/app.min.js'
      },

      dct_open_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= dct_open_app %> ' +
          // '--js <%= dct_open_states %> ' +
          '--js app/doctor/open/states/**.js ' +
          '--js !app/doctor/open/states/**.pageobject.js ' +
          '--js !app/doctor/open/states/**.scenario.js ' +
          '--js !app/doctor/open/states/**.spec.js ' +
          // '--js <%= dct_open_components %> ' +
          '--js app/doctor/open/components/**.js ' +
          '--js !app/doctor/open/components/**.spec.js ' +
          '--js_output_file app/doctor/open/js/app.min.js'
      },

      dct_secured_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY   ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          // '--define=COMPILED=true" '
          '--js <%= dct_secured_app %> ' +
          // '--js <%= dct_secured_states %> ' +
          '--js app/doctor/secured/states/**.js ' +
          '--js !app/doctor/secured/states/**.pageobject.js ' +
          '--js !app/doctor/secured/states/**.scenario.js ' +
          '--js !app/doctor/secured/states/**.spec.js ' +
          // '--js <%= dct_secured_components %> ' +
          '--js app/doctor/secured/components/**.js ' +
          '--js !app/doctor/secured/components/**.spec.js ' +
          '--js_output_file app/doctor/secured/js/app.min.js'
      },

      mgr_open_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= mgr_open_app %> ' +
          // '--js <%= mgr_open_states %> ' +
          '--js app/manager/open/states/**.js ' +
          '--js !app/manager/open/states/**.pageobject.js ' +
          '--js !app/manager/open/states/**.scenario.js ' +
          '--js !app/manager/open/states/**.spec.js ' +
          // '--js <%= mgr_open_components %> ' +
          '--js app/manager/open/components/**.js ' +
          '--js !app/manager/open/components/**.spec.js ' +
          '--js_output_file app/manager/open/js/app.min.js'
      },

      mgr_secured_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= mgr_secured_app %> ' +
          // '--js <%= mgr_secured_states %> ' +
          '--js app/manager/secured/states/**.js ' +
          '--js !app/manager/secured/states/**.pageobject.js ' +
          '--js !app/manager/secured/states/**.scenario.js ' +
          '--js !app/manager/secured/states/**.spec.js ' +
          // '--js <%= mgr_secured_components %> ' +
          '--js app/manager/secured/components/**.js ' +
          '--js !app/manager/secured/components/**.spec.js ' +
          '--js_output_file app/manager/secured/js/app.min.js'
      },

      spr_open_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= spr_open_app %> ' +
          // '--js <%= spr_open_states %> ' +
          '--js app/super/open/states/**.js ' +
          '--js !app/super/open/states/**.pageobject.js ' +
          '--js !app/super/open/states/**.scenario.js ' +
          '--js !app/super/open/states/**.spec.js ' +
          // '--js <%= spr_open_components %> ' +
          '--js app/super/open/components/**.js ' +
          '--js !app/super/open/components/**.spec.js ' +
          '--js_output_file app/super/open/js/app.min.js'
      },

      spr_secured_min: {
        command: 'java -jar closure/compiler.jar ' +
          '--compilation_level WHITESPACE_ONLY ' +
          '--formatting PRETTY_PRINT ' +
          '--language_in ECMASCRIPT5_STRICT ' +
          '--angular_pass ' +                                // inject dependencies automatically
          '--externs closure/externs/angular.js ' +          // angular.d -> angular.module
          '--generate_exports ' +                            // keep @export notated code
          '--manage_closure_dependencies ' +
          '--js closure/library/base.js ' +                  // don't add 'goog.' stuff to script
          // '--js closure/library/deps.js ' +                  // 
          '--js <%= spr_secured_app %> ' +
          // '--js <%= spr_secured_states %> ' +
          '--js app/super/secured/states/**.js ' +
          '--js !app/super/secured/states/**.pageobject.js ' +
          '--js !app/super/secured/states/**.scenario.js ' +
          '--js !app/super/secured/states/**.spec.js ' +
          // '--js <%= spr_secured_components %> ' +
          '--js app/super/secured/components/**.js ' +
          '--js !app/super/secured/components/**.spec.js ' +
          '--js_output_file app/super/secured/js/app.min.js'
      }
    },

  	watch: {
      /**
       *
       * MANUFACTORY
       */
  		mnf_open_js: {
  		  options: { 
  		    livereload: true
  		  },
        files: [
          'app/manufactory/open/js/app.js',
          'app/manufactory/open/states/**/*.js',
          '!app/manufactory/open/states/**/*.pageobject.js',
          '!app/manufactory/open/states/**/*.scenario.js',
          '!app/manufactory/open/states/**/*.spec.js',
          'app/manufactory/open/components/**/*.js',
          '!app/manufactory/open/components/**/*.spec.js'
        ],
  		  tasks: ['shell:mnf_open_min']
  		},
  		mnf_open_pages: {
  		  options: { 
  		    livereload: true
  		  },
        files: [
          'app/manufactory/open/index.html',
          'app/manufactory/open/**/*.html',
          'app/manufactory/open/**/*.css'
        ],
  		  tasks: []
  		},
      mnf_secured_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/manufactory/secured/js/app.js',
          'app/manufactory/secured/states/**/*.js',
          '!app/manufactory/secured/states/**/*.pageobject.js',
          '!app/manufactory/secured/states/**/*.scenario.js',
          '!app/manufactory/secured/states/**/*.spec.js',
          'app/manufactory/secured/components/**/*.js',
          '!app/manufactory/secured/components/**/*.spec.js'
        ],
        tasks: ['shell:mnf_secured_min']
      },
      mnf_secured_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/manufactory/secured/index.html',
          'app/manufactory/secured/**/*.html',
          'app/manufactory/secured/**/*.css'
        ],
        tasks: []
      },

      /**
       *
       * DOCTOR
       */
      dct_open_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/doctor/open/js/app.js',
          'app/doctor/open/states/**/*.js',
          '!app/doctor/open/states/**/*.pageobject.js',
          '!app/doctor/open/states/**/*.scenario.js',
          '!app/doctor/open/states/**/*.spec.js',
          'app/doctor/open/components/**/*.js',
          '!app/doctor/open/components/**/*.spec.js'
        ],
        tasks: ['shell:dct_open_min']
      },
      dct_open_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/doctor/open/index.html',
          'app/doctor/open/**/*.html',
          'app/doctor/open/**/*.css'
        ],
        tasks: []
      },
      dct_secured_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/doctor/secured/js/app.js',
          'app/doctor/secured/states/**/*.js',
          '!app/doctor/secured/states/**/*.pageobject.js',
          '!app/doctor/secured/states/**/*.scenario.js',
          '!app/doctor/secured/states/**/*.spec.js',
          'app/doctor/secured/components/**/*.js',
          '!app/doctor/secured/components/**/*.spec.js'
        ],
        tasks: ['shell:dct_secured_min']
      },
      dct_secured_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/doctor/secured/index.html',
          'app/doctor/secured/**/*.html',
          'app/doctor/secured/**/*.css'
        ],
        tasks: []
      },

      /**
       *
       * MANAGER
       */
      mgr_open_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/manager/open/js/app.js',
          'app/manager/open/states/**/*.js',
          '!app/manager/open/states/**/*.pageobject.js',
          '!app/manager/open/states/**/*.scenario.js',
          '!app/manager/open/states/**/*.spec.js',
          'app/manager/open/components/**/*.js',
          '!app/manager/open/components/**/*.spec.js'
        ],
        tasks: ['shell:mgr_open_min']
      },
      mgr_open_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/manager/open/index.html',
          'app/manager/open/**/*.html',
          'app/manager/open/**/*.css'
        ],
        tasks: []
      },
      mgr_secured_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/manager/secured/js/app.js',
          'app/manager/secured/states/**/*.js',
          '!app/manager/secured/states/**/*.pageobject.js',
          '!app/manager/secured/states/**/*.scenario.js',
          '!app/manager/secured/states/**/*.spec.js',
          'app/manager/secured/components/**/*.js',
          '!app/manager/secured/components/**/*.spec.js'
        ],
        tasks: ['shell:mgr_secured_min']
      },
      mgr_secured_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/manager/secured/index.html',
          'app/manager/secured/**/*.html',
          'app/manager/secured/**/*.css'
        ],
        tasks: []
      },

      /**
       *
       * SUPER
       */
      spr_open_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/super/open/js/app.js',
          'app/super/open/states/**/*.js',
          '!app/super/open/states/**/*.pageobject.js',
          '!app/super/open/states/**/*.scenario.js',
          '!app/super/open/states/**/*.spec.js',
          'app/super/open/components/**/*.js',
          '!app/super/open/components/**/*.spec.js'
        ],
        tasks: ['shell:spr_open_min']
      },
      spr_open_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/super/open/index.html',
          'app/super/open/**/*.html',
          'app/super/open/**/*.css'
        ],
        tasks: []
      },
      spr_secured_js: {
        options: { 
          livereload: true
        },
        files: [
          'app/super/secured/js/app.js',
          'app/super/secured/states/**/*.js',
          '!app/super/secured/states/**/*.pageobject.js',
          '!app/super/secured/states/**/*.scenario.js',
          '!app/super/secured/states/**/*.spec.js',
          'app/super/secured/components/**/*.js',
          '!app/super/secured/components/**/*.spec.js'
        ],
        tasks: ['shell:spr_secured_min']
      },
      spr_secured_pages: {
        options: { 
          livereload: true
        },
        files: [
          'app/super/secured/index.html',
          'app/super/secured/**/*.html',
          'app/super/secured/**/*.css'
        ],
        tasks: []
      }
  	},

  	open: {
  		server: {
  			path: 'http://localhost:8080/'
  		}
  	},

    // Distribution step 1: empty distribution folder
    clean: {
      options: { force: true },
      all: { src: ["../app/**/*"] }
    },

    // Distribution step 2: copy resoruce from source
    copy: {
        others: {
          expand: true, 
          cwd: 'app/', 
          src: ['**', 
            '!**/doctor/**',
            '!**/manager/**',
            '!**/manufactory/**',
            '!**/super/**'
          ], 
          dest: '../app/'
        },
        doctor_others: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/doctor/**','!**/doctor/**/*.js'], 
          dest: '../app/'
        },
        doctor_js: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/doctor/**/app.min.js'], 
          dest: '../app/'
        },
        manager_others: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/manager/**','!**/manager/**/*.js'], 
          dest: '../app/'
        },
        manager_js: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/manager/**/app.min.js'], 
          dest: '../app/'
        },
        manufactory_others: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/manufactory/**','!**/manufactory/**/*.js'], 
          dest: '../app/'
        },
        manufactory_js: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/manufactory/**/app.min.js'], 
          dest: '../app/'
        },
        super_others: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/super/**','!**/super/**/*.js'], 
          dest: '../app/'
        },
        super_js: {
          expand: true, 
          cwd: 'app/', 
          src: ['**/super/**/app.min.js'], 
          dest: '../app/'
        },
    },

    // Distribution step 3: remove empty folders
    cleanempty: {
      options: {
        force: true,
      },
      dist: {
        options: {
          files: false,
        },
        src: ['../app/**/*']
      }
    },

  });

  grunt.loadNpmTasks('grunt-shell');
  grunt.loadNpmTasks('grunt-open');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-cleanempty');

  grunt.registerTask('compile', [
      'shell:mnf_open_min', 
      'shell:mnf_secured_min',
      'shell:dct_open_min', 
      'shell:dct_secured_min',
      'shell:mgr_open_min', 
      'shell:mgr_secured_min',
      'shell:spr_open_min', 
      'shell:spr_secured_min',
      'clean',
      'copy',
      'cleanempty'
    ]);
  grunt.registerTask('default', [
      'connect', 
      'open', 
      'watch'
    ]);
};
