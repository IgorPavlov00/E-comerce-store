const express=require("express");
const cors=require("cors");
const bodyparser=require("body-parser");


const app=express();

app.use(express.static("public"));
app.use(bodyparser.urlencoded({extended:false}));
app.use(bodyparser.json());
app.use(cors({origin:true,credentials:true}));

const stripe=require("stripe")("sk_test_51OkQ5MBFPXnaeo7Vj5BMAmCoqX6rLCTSv9tUf8x7J4J5OOfFVYpFSskg8o9och2EPPLvJvPSr1Ozl3kBpeKqPPku00TrFPAazi");

app.post("/checkout",async (req,res,next)=> {
  console.log(req.body.items);
  try{
    const session= await stripe.checkout.sessions.create({
      line_items:req.body.items.map((item)=>({

        price_data: {
          currency: 'usd',
          product_data: {
            name: item.name,
            images: [item.product]
          },
          unit_amount: item.price * 100, // Assuming item.price is in dollars
        },
           quantity:item.quantity,
      })),
      mode:"payment",
      success_url:"http://localhost:4242/success.html",
      cancel_url:"http://localhost:4242/cancel.html",
    });

    res.status(200).json(session);
  } catch (error){
        next(error)
  }



});
app.listen(4242,()=>console.log("app is runnin on 4242"));
