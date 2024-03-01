import { mount } from "@vue/test-utils";
import HomeView from "@/views/HomeView.vue";
import LaptopTable from "@/components/LaptopTable.vue";

describe("HomeView.vue", () => {
  it("renders the LaptopTable component with correct props", () => {
    const laptops = [
      { id: 1, name: "Laptop 1", price: 1000 },
      { id: 2, name: "Laptop 2", price: 1500 },
      { id: 3, name: "Laptop 3", price: 1200 }
    ];
    const wrapper = mount(HomeView, {
      data() {
        return {
          laptops: laptops,
          laptopsLoading: false,
          currentSort: "name",
          searchInput: "",
          mobile: false,
          currentFilters: {
            showStatusText: true,
            showOnly: false
          }
        };
      }
    });

    const laptopTable = wrapper.findComponent(LaptopTable);

    expect(laptopTable.exists()).toBe(true);
    expect(laptopTable.props("laptops")).toEqual(laptops);
    expect(laptopTable.props("currentSort")).toBe("name");
    expect(laptopTable.props("searchInput")).toBe("");
    expect(laptopTable.props("mobile")).toBe(false);
    expect(laptopTable.props("toggleDetails")).toBe(true);
  });

  it("displays the correct laptop count when searchInput is empty", () => {
    const laptops = [
      { id: 1, name: "Laptop 1", price: 1000 },
      { id: 2, name: "Laptop 2", price: 1500 },
      { id: 3, name: "Laptop 3", price: 1200 }
    ];
    const wrapper = mount(HomeView, {
      data() {
        return {
          laptops: laptops,
          laptopsLoading: false,
          currentSort: "name",
          searchInput: "",
          mobile: false,
          currentFilters: {
            showStatusText: true,
            showOnly: false
          }
        };
      }
    });

    const laptopCountText = wrapper.find(".themed-button p:last-child");

    expect(laptopCountText.text()).toBe(`Er zijn ${laptops.length} laptops geladen.`);
  });

  // Add more tests as needed...
});