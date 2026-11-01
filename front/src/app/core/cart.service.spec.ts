import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CartService } from './cart.service';
import { Product } from './models';

const shoe: Product = {
  id: 1, type: 'shoe', name: 'Air', description: 'Red', brand: 'Jordan',
  size: '42', color: 'Red', price: 80, imagePath: 'a.png',
};
const jean: Product = { ...shoe, type: 'jean', name: 'Denim', price: 70.5 };

describe('CartService', () => {
  let cart: CartService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    cart = TestBed.inject(CartService);
    http = TestBed.inject(HttpTestingController);
  });

  it('merges repeat adds of one product but keeps same id of another type separate', () => {
    cart.add(shoe);
    cart.add(shoe, 2);
    cart.add(jean);
    expect(cart.items.length).toBe(2);
    expect(cart.items[0].quantity).toBe(3);
  });

  it('limits quantity to the range the API accepts', () => {
    cart.add(shoe, 99);
    expect(cart.items[0].quantity).toBe(20);
    cart.setQuantity(cart.items[0], 0);
    expect(cart.items[0].quantity).toBe(1);
  });

  it('charges shipping below $150 and none from $150', () => {
    expect(cart.totalCents).toBe(0);
    cart.add(shoe);
    expect(cart.shippingCents).toBe(2000);
    expect(cart.totalCents).toBe(10000);
    cart.add(jean);
    expect(cart.subtotalCents).toBe(15050);
    expect(cart.shippingCents).toBe(0);
  });

  it('restores the cart from storage', () => {
    cart.add(shoe, 2);
    const reopened = new CartService(TestBed.inject(HttpClient));
    expect(reopened.items.length).toBe(1);
    expect(reopened.items[0].quantity).toBe(2);
  });

  it('sends only ids and quantities to checkout, and keeps the cart', () => {
    cart.add(shoe, 2);
    let url = '';
    cart.startCheckout().subscribe(u => (url = u));
    const request = http.expectOne('/api/checkout');
    expect(request.request.body).toEqual({ items: [{ type: 'shoe', id: 1, quantity: 2 }] });
    request.flush({ url: 'https://stripe.test' });
    expect(url).toBe('https://stripe.test');
    expect(cart.items.length).toBe(1);
  });
});
