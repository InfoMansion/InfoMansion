<<<<<<< HEAD
//.eslintrc.js

module.exports = {
    env: {
      browser: true,
      es6: true,
      node: true,
    },
    extends:['airbnb','prettier/react', 'eslint:recommended','plugin:prettier/recommended'],
    // prettier/react 추가
    rules:{
      'react/jsx-filename-extension': 
      ['error', { 'extensions': [".js", ".jsx"] }],
    }
  };
=======
module.exports = {
  env: {
    browser: true,
    es6: true,
    node: true,
  },

  extends: [
    'airbnb',
    'prettier/react',
    'eslint:recommended',
    'plugin:prettier/recommended',
  ],
  // prettier/react 추가
  rules: {
    'react/jsx-filename-extension': ['error', { extensions: ['.js', '.jsx'] }],
    'no-restricted-imports': [
      'error',
      {
        patterns: ['@mui/*/*/*', '!@mui/material/test-utils/*'],
      },
    ],
  },
};
>>>>>>> 65b6157af784c6b7a9183a674af612141d20020e
