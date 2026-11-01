export type ProductType = 'shoe' | 'jean';

export interface Product {
  id: number;
  type: ProductType;
  name: string;
  description: string;
  brand: string;
  size: string;
  color: string;
  price: number;
  imagePath: string;
}

export interface CartItem {
  id: number;
  type: ProductType;
  name: string;
  description: string;
  size: string;
  imagePath: string;
  price: number;
  quantity: number;
}

export interface User {
  id: number;
  name: string;
  lastname: string;
  email: string;
}

export interface AuthResponse {
  token: string | null;
  user: User;
  verificationRequired: boolean;
}

export interface OrderItem {
  type: ProductType;
  productId: number;
  name: string;
  description: string;
  imagePath: string;
  unitPriceCents: number;
  quantity: number;
}

export interface Order {
  id: number;
  status: 'PENDING' | 'PAID';
  createdAt: string;
  subtotalCents: number;
  shippingCents: number;
  totalCents: number;
  items: OrderItem[];
}

export function sizeLabel(product: { type: ProductType; size: string }): string {
  return product.type === 'shoe' ? `EU ${product.size}` : `W${product.size}`;
}

/** Reads the friendly message the API sends with errors. */
export function errorMessage(error: unknown, fallback: string): string {
  const message = (error as { error?: { message?: unknown } })?.error?.message;
  return typeof message === 'string' ? message : fallback;
}
