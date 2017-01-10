require('babel-register')({
    plugins: ['transform-async-to-generator', 'transform-es2015-modules-commonjs']
});
require('./app/scripts/service/app.js');
require('./app/scripts/service/server.js');