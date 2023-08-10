export interface Env {
  VOTING_URL: string;
}

export default {
  async fetch(
    request: Request,
    env: Env,
    _: ExecutionContext
  ): Promise<Response> {
    try {
      console.log(
        `Request incoming: ${request.headers.get('User-Agent')}`
      );

      const response = await fetch(env.VOTING_URL);
      const result = await response.json();
      return new Response('OK: ' + JSON.stringify(result));
    } catch (error) {
      return new Response('Err: ' + JSON.stringify(error));
    }
  },

  async scheduled(
    conttroller: ScheduledController,
    env: Env,
    _: ExecutionContext
  ) {
    try {
      conttroller.noRetry();
      const response = await fetch(env.VOTING_URL);
      const result = await response.json();
      console.log(`Success: ${JSON.stringify(result)}`);
    } catch (error) {
      console.log(`Err: ${JSON.stringify(error)}`);
    }
  },
};
