const path = require('path');
const config = require('../config');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
//配置静态资源路径
exports.assetsPath = function (_path) {
    let assetsSubDirectory;
    if(process.env.NODE_ENV === 'production') {
        assetsSubDirectory = config.build.assetsSubDirectory;
    } else if(process.env.NODE_ENV === 'integration') {
        assetsSubDirectory = config.test.assetsSubDirectory;
    } else {
        assetsSubDirectory = config.dev.assetsSubDirectory;
    }
    return path.posix.join(assetsSubDirectory, _path);
};
//生成cssLoaders用于加载.vue文件中的样式
exports.cssLoaders = function (options) {
    options = options || {};

    // generate loader string to be used with extract text plugin
    function generateLoaders(loaders) {
        let sourceLoader = loaders.map(function (loader) {
            let extraParamChar;
            if (/\?/.test(loader)) {
                loader = loader.replace(/\?/, '-loader?');
                extraParamChar = '&'
            } else {
                loader = loader + '-loader';
                extraParamChar = '?';
            }
            return loader + (options.sourceMap ? extraParamChar + 'sourceMap' : '');
        }).join('!');

        // Extract CSS when that option is specified
        // (which is the case during production build)
        if (options.extract) {
            return ExtractTextPlugin.extract({
                use: sourceLoader,
                fallback: 'vue-style-loader',
                publicPath: '../../'
            });
        } else {
            return ['vue-style-loader', sourceLoader].join('!');
        }
    }

    // http://vuejs.github.io/vue-loader/en/configurations/extract-css.html
    return {
        css: generateLoaders(['css']),
        postcss: generateLoaders(['css']),
        less: generateLoaders(['css', 'less']),
        sass: generateLoaders(['css', 'sass'], {indentedSyntax: true}),
        scss: generateLoaders(['css', 'sass']),
        stylus: generateLoaders(['css', 'stylus']),
        styl: generateLoaders(['css', 'stylus'])
    }
};
//生成styleLoaders用于加载不在.vue文件中的单独存在的样式文件
// Generate loaders for standalone style files (outside of .vue)
exports.styleLoaders = function (options) {
    let output = [];
    let loaders = exports.cssLoaders(options);
    for (let extension in loaders) {
        let loader = loaders[extension];
        output.push({
            test: new RegExp('\\.' + extension + '$'),
            loader: loader
        });
    }
    return output
};
