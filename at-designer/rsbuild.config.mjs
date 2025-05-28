import { defineConfig } from '@rsbuild/core';
import { pluginReact } from '@rsbuild/plugin-react';

export default defineConfig({
  plugins: [pluginReact()],
  html: {
    favicon: './src/logo.svg', // 或 public/logo.svg
    title: 'at-designer',
  },
});
