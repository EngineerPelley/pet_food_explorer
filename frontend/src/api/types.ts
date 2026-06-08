// Shapes returned by the backend REST API. These mirror the Java records in
// com.petfood.explorer.product.

export interface ProductSummary {
  foodId: number;
  name: string;
  brandName: string;
  productTypeName: string | null;
  caloriesPerCup: number | null;
}

export interface BrandSummary {
  brandId: number;
  name: string;
  description: string | null;
}

export interface ProductTypeRef {
  productTypeId: number;
  name: string;
}

export interface PetTypeRef {
  petTypeId: number;
  name: string;
}

export interface IngredientView {
  ingredientId: number;
  name: string;
  source: string | null;
  labelPosition: number | null;
  percentage: number | null;
}

export interface ProductDetail {
  foodId: number;
  name: string;
  description: string | null;
  caloriesPerCup: number | null;
  brand: BrandSummary;
  productType: ProductTypeRef | null;
  petTypes: PetTypeRef[];
  ingredients: IngredientView[];
}
