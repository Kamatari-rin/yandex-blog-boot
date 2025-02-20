const UnoCSS = require('@unocss/webpack').default;

module.exports = {
  webpack: {
    plugins: [UnoCSS()],
  },
};
