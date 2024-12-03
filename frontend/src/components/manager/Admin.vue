<!--<script setup>-->

<!--</script>-->

<!--<template>-->
<!--<h1>Admin</h1>-->
<!--</template>-->

<!--<style scoped>-->

<!--</style>-->
<template>

  <el-date-picker
      v-model="value1"
      type="date"
      placeholder="Pick a day"
      :size="size"
  />
<!--  <div class="demo-date-picker">-->
<!--    <div class="block">-->
<!--      <span class="demonstration">Default</span>-->
<!--      <el-date-picker-->
<!--          v-model="value1"-->
<!--          type="date"-->
<!--          placeholder="Pick a day"-->
<!--          :size="size"-->
<!--      />-->
<!--    </div>-->
<!--  </div>-->
</template>

<script  setup>
import { ref } from 'vue'

const size = ref<'default' | 'large' | 'small'>('default')

const value1 = ref('')

</script>

