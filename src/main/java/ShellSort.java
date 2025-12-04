import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShellSort {
    
    // Main function to sort array using ShellSort
    public void sort(int arr[]) {
        int n = arr.length;

        // Start with a big gap, then reduce the gap
        for (int gap = n / 2; gap > 0; gap /= 2) {
            System.out.print("With gap size: "+gap+", arr ends with -> ");
            // Do a gapped insertion sort for this gap size.
            // The first gap elements a[0..gap-1] are already in gapped order
            // keep adding one more element until the entire array is gap sorted
            for (int i = gap; i < n; i += 1) {
                
                // add a[i] to the elements that have been gap sorted
                // save a[i] in temp and make a hole at position i
                int temp = arr[i];
                
                int j;
                
                // shift earlier gap-sorted elements up until the correct location for a[i] is found
                for (j = i; j >= gap && arr[j - gap] < temp; j--) {
                    arr[j] = arr[j - gap];
                }
                
                // put temp (the original a[i]) in its correct location
                arr[j] = temp;
            }
            System.out.println(Arrays.toString(arr));
        }
    }

    // Driver method
    public static void main(String args[]) {
        int arr[] = {1,2,3,4,5,6,7,8};
        arr = random(arr);

        System.out.println("\n\nArray before sorting:");
        System.out.println(Arrays.toString(arr)+"\n");

        ShellSort ob = new ShellSort();
        ob.sort(arr);

        System.out.println("\nArray after sorting:");
        System.out.println(Arrays.toString(arr)+"\n");
    }

    // Randomizes an arr
    public static int[] random(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for(int a:arr){
            list.add(a);
        }
        Collections.shuffle(list);
        return list.stream().mapToInt(i -> i).toArray();
    }
}