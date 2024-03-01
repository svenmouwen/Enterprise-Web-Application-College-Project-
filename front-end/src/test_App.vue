<template>
  <div>
    <p>{{ message }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: "Hello, World!"
    };
  }
};
</script>

<test>
import { mount } from "@vue/test-utils";
import App from "@/App.vue";

describe("App.vue", () => {
  it("renders the correct message", () => {
    const wrapper = mount(App);
    expect(wrapper.find("p").text()).toBe("Hello, World!");
  });
});
</test>