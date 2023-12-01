<template>
    <div class="Searching" style="height: 100%; width: 100%; ">
        <SearchZone style="width: 40%; margin: 50% auto; margin-top:40vh"/>
    </div>
</template>

<script>
import SearchZone from './SearchZone.vue';
export default {
    data: function () {
        return{
            
        }
    },
    components: { SearchZone }
}
</script>

<style>
.Searching{
    position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  min-height: 100%;
  background: url("../views/Search_background.jpg") fixed no-repeat;
  background-size: cover;
}
</style>