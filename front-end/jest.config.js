module.exports = {
    moduleFileExtensions: [
        "js",
        "json",
        "vue",
    ],
    transform: {
        // process *.vue files with vue-jest
        "^.+\\.vue$": "vue-jest",
        // process js files with babel-jest
        "^.+\\.js$": "babel-jest",
    },
    // setupFiles: ["<rootDir>/tests/unit/setup"],
    moduleNameMapper: {
        "^@/(.*)$": "<rootDir>/src/$1",
        "\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$": "<rootDir>/__mocks__/fileMock.js",
        "^vue$": "vue/dist/vue.esm-bundler.js"
    },
    testEnvironment: 'jest-environment-jsdom',
    snapshotSerializers: [
        'jest-serializer-vue'
    ]
};