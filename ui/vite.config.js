import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  // 在codesandbox中需要用443来保证开发时不会重复刷新
  // server: {
  //   hmr: {
  //     port: 443
  //   }
  // },
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  build: {
    outDir: '../src/main/resources/static', // Output directory
    rollupOptions: {
      output: {
        entryFileNames: 'assets/[name].js', // js Output file name
        assetFileNames: 'assets/[name].[ext]', // Other file output name
      },
    },
  }
})
