using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace Lab2App.Pages   // ⚠️ MUST match your project name
{
    public class IndexModel : PageModel
    {
        [BindProperty]
        public string? UserName { get; set; }

        [BindProperty]
        public string? Language { get; set; }

        public string? Message { get; set; }

        public void OnPost()
        {
            Message = $"Your name is {UserName} and your favourite programming language is {Language}";
        }
    }
}