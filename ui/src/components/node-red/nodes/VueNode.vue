<template>
  <div class="vue-node-collapse" :style="properties.style">
    <div>[{{ properties.command }}]</div>
    <div class="vue-node-operator">
      <div>target：{{ properties.target }}</div>
    </div>
    <div class="vue-node-operator">
      <div>value：{{ properties.value }}</div>
    </div>
    <div class="vue-node-operator">
      <div>description：{{ properties.description }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    properties: {
      type: Object,
      default: () => ({
        t: 1,
        style: {}
      })
    }
  }
}
</script>
<style scoped>
.vue-node-collapse {
  width: 250px;
  height: 100px;
  border: 1px solid #444;
  box-sizing: border-box;
}

.vue-node-operator {
  display: flex;
  font-size: 14px;
  color: #333;
}
</style>