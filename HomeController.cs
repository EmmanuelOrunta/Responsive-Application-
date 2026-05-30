using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using AvailabilityApp.Models;

namespace AvailabilityApp.Controllers;

public class HomeController : Controller
{
    public IActionResult Index()
    {
        return View();
    }

    public IActionResult Privacy()
    {
        return View();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }

    
    [HttpGet]
    public IActionResult CheckAvailability()
    {
        // Simulate delay (like server processing)
        System.Threading.Thread.Sleep(2000);

        var result = new
        {
            status = "Available ✔️"
        };

        return Json(result);
    }
    public IActionResult CheckAvailabilityPartial()
{
    System.Threading.Thread.Sleep(2000);

    return Content("<p>Available ✔️</p>", "text/html");
}
}