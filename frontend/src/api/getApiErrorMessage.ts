export function getApiErrorMessage(error: unknown, fallback: string): string {
  if (typeof error === 'object' && error !== null && 'response' in error) {
    const response = (error as { response?: { data?: unknown } }).response
    if (
      response?.data &&
      typeof response.data === 'object' &&
      'message' in response.data &&
      typeof (response.data as { message?: unknown }).message === 'string'
    ) {
      return (response.data as { message: string }).message
    }
  }
  if (error instanceof Error) {
    return error.message
  }
  return fallback
}
