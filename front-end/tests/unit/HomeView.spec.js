import {mount, shallowMount} from '@vue/test-utils';
import {describe, expect, it, test} from '@jest/globals';
import LaptopDetails from '@/components/LaptopDetails.vue'
import HomeView from '@/views/HomeView.vue'



describe('LaptopDetails', () => {
    test('sets creating to true when path includes "new" and currentUser is defined', () => {
        const wrapper = mount(LaptopDetails, {
            propsData: {
                currentLaptop: {},
                currentUser: {},
            },
            global: {
                mocks: {
                    $route: {
                        fullPath: '/new',
                    },
                },
            },
        })

        expect(wrapper.vm.creating).toBe(true)
    })

    test('sets laptop to currentLaptop on mount', async () => {
        const currentLaptop = { ean: '123456789' }
        const wrapper = mount(LaptopDetails, {
            propsData: {
                currentLaptop,
                currentUser: {},
            },
        })

        await wrapper.vm.$nextTick()

        expect(wrapper.vm.laptop).toBe(currentLaptop)
    })

    test('resetLaptop sets editedLaptop to a copy of laptop', () => {
        const laptop = { ean: '123456789' }
        const wrapper = mount(LaptopDetails, {
            propsData: {
                currentLaptop: laptop,
                currentUser: {},
            },
        })

        wrapper.vm.laptop = laptop
        wrapper.vm.resetLaptop()

        expect(wrapper.vm.editedLaptop).toEqual(laptop)
        expect(wrapper.vm.editedLaptop).not.toBe(laptop) // Ensure it's a copy, not the same object
    })
})
describe('HomeView', () => {


    it('does a wrapper exist', () => {
        const wrapper = shallowMount(HomeView);
        expect(wrapper.exists()).toBe(true);
    });

// checks if the component renders correctly. Uses Jest's snapshot testing for a quick visual check.
it('renders correctly', () => {
    const wrapper = shallowMount(HomeView);
    expect(wrapper.element).toMatchSnapshot();
});

    //  verifies component default data
    it('initializes with default data', () => {
        const wrapper = mount(HomeView);
        expect(wrapper.vm.laptops).toHaveLength(10);
        expect(wrapper.vm.filteredLaptops).toHaveLength(10);
        // Add more expectations based on your data initialization
    });

    it('updates searchInput on input change', async () => {
        const wrapper = mount(HomeView);
        const searchInput = wrapper.find('.searchbar-input input');

        await searchInput.setValue('377');
        expect(wrapper.vm.searchInput).toBe('');
    });
})
