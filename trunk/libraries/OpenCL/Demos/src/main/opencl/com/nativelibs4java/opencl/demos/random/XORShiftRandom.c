#ifndef NUMBERS_COUNT
#define NUMBERS_COUNT nNumbersArg
#endif

#ifndef WORK_ITEMS_COUNT
#define WORK_ITEMS_COUNT get_global_size(0)
#endif

/**
 * Logic copied from http://en.wikipedia.org/wiki/Xorshift
 * Requires 4 initial random seeds for each work item
 */
__kernel void gen_numbers(__global uint4* seeds, size_t nNumbersArg, __global uint* output)
{
	const uint iWorkItem = get_global_id(0);
	const uint seedsOffset = iWorkItem;
#if 1
#define nNumbers NUMBERS_COUNT
#define nWorkItems WORK_ITEMS_COUNT
#define nNumbersByWorkItem nNumbers / nWorkItems
#define REMAINDER nNumbers - nNumbersByWorkItem * WORK_ITEMS_COUNT
	uint nNumbersInThisWorkItem = nNumbersByWorkItem;
	if (iWorkItem == nWorkItems - 1)
		nNumbersInThisWorkItem += REMAINDER;
#else
	const uint nNumbers = nNumbersArg;
	const size_t nWorkItems = get_global_size(0);
	const uint nNumbersByWorkItem = nNumbers / nWorkItems;
	uint nNumbersInThisWorkItem = nNumbersByWorkItem;
	if (iWorkItem == nWorkItems - 1)
		nNumbersInThisWorkItem += nNumbers - nNumbersByWorkItem * nWorkItems;
#endif
	
	const uint outputOffset = iWorkItem * nNumbersByWorkItem;
	//output[iWorkItem] = nNumbersArg;//seeds[iWorkItem].x;
	//if (true)
	//	return;
	
	uint4 seed = seeds[seedsOffset];
	for (int i = 0; i < nNumbersInThisWorkItem; i++) {
		uint t = seed.x ^ (seed.x << 11);
		seed.xyz = seed.yzw;
		output[outputOffset + i] = seed.w = (seed.w ^ (seed.w >> 19)) ^ (t ^ (t >> 8));
	}
	seeds[seedsOffset] = seed;
	
}

